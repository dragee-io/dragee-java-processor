package io.dragee.rules.fixes.issue9;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class MethodReturnExcessDependencyTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "fixes", "issue9");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "fixes", "issue9");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("Test.java"),
                SOURCE_FOLDER.resolve("TestId.java")
        );

        return compiler.executeProcessor();
    }

    private static String contentOfChildDragee(Compiler.Result actualResult) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("TestId.json"));
    }

    /**
     * Issue #9 : <a href="https://github.com/dragee-io/dragee-java-processor/issues/9">...</a>
     * Test has a dependency to TestId.
     * TestId was wrongfully stating that it had a dependency to Test.
     * This was due to a bad HasType.isOrGeneric, based on a simple contains.
     */
    @Test
    void dragee_child_must_not_have_parent_on_dependency_when_name_contains_parent_name() {
        Compiler.Result actualResult = executeProcessor();

        String actualChildContent = contentOfChildDragee(actualResult);

        assertThatJson(actualChildContent)
                .inPath("$.depends_on")
                .isObject()
                .containsOnlyKeys(
                        "io.dragee.rules.fixes.issue9.TestId");
    }
}
