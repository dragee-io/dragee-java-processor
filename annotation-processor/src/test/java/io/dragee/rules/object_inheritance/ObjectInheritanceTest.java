package io.dragee.rules.object_inheritance;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class ObjectInheritanceTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "object_inheritance");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "object_inheritance");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("GrandParent.java"),
                SOURCE_FOLDER.resolve("Parent.java"),
                SOURCE_FOLDER.resolve("Child.java"),
                SOURCE_FOLDER.resolve("SecondGrandParent.java"),
                SOURCE_FOLDER.resolve("SecondParent.java"),
                SOURCE_FOLDER.resolve("SecondChild.java")
        );

        return compiler.executeProcessor();
    }

    private static String contentOfDragee(Compiler.Result actualResult, String expectedFileName) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve(expectedFileName));
    }

    @Test
    void profile_dragee_can_be_inherited() {
        Compiler.Result actualResult = executeProcessor();

        String actualContent = contentOfDragee(actualResult, "Child.json");

        assertThatJson(actualContent)
                .inPath("$.profile")
                .isEqualTo("testing/inheritable_type");

        assertThat(actualResult.hasDrageeFile(OUTPUT_FOLDER.resolve("GrandParent.json"))).isTrue();
        assertThat(actualResult.hasDrageeFile(OUTPUT_FOLDER.resolve("Parent.json"))).isTrue();
    }

    @Test
    void profile_dragee_is_not_inherited() {
        Compiler.Result actualResult = executeProcessor();

        String actualContent = contentOfDragee(actualResult, "SecondGrandParent.json");

        assertThatJson(actualContent)
                .inPath("$.profile")
                .isEqualTo("testing/type_one");

        assertThat(actualResult.hasDrageeFile(OUTPUT_FOLDER.resolve("SecondParent.json"))).isFalse();
        assertThat(actualResult.hasDrageeFile(OUTPUT_FOLDER.resolve("SecondChild.json"))).isFalse();
    }
}
