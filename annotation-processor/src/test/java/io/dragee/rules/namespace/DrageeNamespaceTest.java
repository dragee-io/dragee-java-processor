package io.dragee.rules.namespace;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class DrageeNamespaceTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "namespace");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "namespace");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("AnObject.java")
        );

        return compiler.executeProcessor();
    }

    private static String contentOfDragee(Compiler.Result actualResult) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("AnObject.json"));
    }

    @Test
    void dragee_must_have_a_namespace() {
        Compiler.Result actualResult = executeProcessor();

        String actualContent = contentOfDragee(actualResult);

        assertThatJson(actualContent)
                .inPath("$.kind_of")
                .isEqualTo("some_namespace/some_concept");
    }

}
