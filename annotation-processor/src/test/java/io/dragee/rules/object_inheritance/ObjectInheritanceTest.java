package io.dragee.rules.object_inheritance;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class ObjectInheritanceTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "object_inheritance");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "object_inheritance");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("GrandParent.java"),
                SOURCE_FOLDER.resolve("Parent.java"),
                SOURCE_FOLDER.resolve("Child.java")
        );

        return compiler.executeProcessor();
    }

    private static String contentOfDragee(Compiler.Result actualResult) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("Child.json"));
    }

    @Test
    void kind_of_dragee_can_be_inherited() {
        Compiler.Result actualResult = executeProcessor();

        String actualContent = contentOfDragee(actualResult);

        assertThatJson(actualContent)
                .inPath("$.kind_of")
                .isEqualTo("testing/kind_one");
    }

}
