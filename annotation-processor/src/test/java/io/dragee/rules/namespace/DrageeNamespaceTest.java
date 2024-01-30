package io.dragee.rules.namespace;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class DrageeNamespaceTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "namespace");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "namespace");

    private static Compiler.Result executeProcessor(String drageeName) {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve(drageeName)
        );

        return compiler.executeProcessor();
    }

    private static String contentOfDragee(Compiler.Result actualResult) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("AnObject.json"));
    }

    @Test
    void dragee_must_have_a_namespace() {
        Compiler.Result actualResult = executeProcessor("AnObject.java");

        String actualContent = contentOfDragee(actualResult);

        assertThatJson(actualContent)
                .inPath("$.kind_of")
                .isEqualTo("some_namespace/some_concept");
    }

    private static String contentOfDrageeWithSubNamespace(Compiler.Result actualResult){
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("AnObjectWithSubNamespace.json"));
    }

    @Test
    void dragee_must_have_multiple_namespaces(){
        Compiler.Result actualResult = executeProcessor("AnObjectWithSubNamespace.java");
        String actualContent = contentOfDrageeWithSubNamespace(actualResult);

        assertThatJson(actualContent)
                .inPath("$.kind_of")
                .isEqualTo("some_namespace/some_sub_namespace/some_other_concept");
    }

    private static String contentOfDrageeWithSubSubNamespace(Compiler.Result actualResult){
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("AnObjectWithSubSubNamespace.json"));
    }
    @Test
    void dragee_must_have_multiple_namespaces_with_two_levels_of_subnamespaces(){
        Compiler.Result actualResult = executeProcessor("AnObjectWithSubSubNamespace.java");
        String actualContent = contentOfDrageeWithSubSubNamespace(actualResult);

        assertThatJson(actualContent)
                .inPath("$.kind_of")
                .isEqualTo("some_namespace/some_other_sub_namespace/asub_sub_namespace/some_dragee_in_sub_sub_namespace");
    }
}
