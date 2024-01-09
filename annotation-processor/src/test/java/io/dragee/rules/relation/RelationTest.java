package io.dragee.rules.relation;

import io.dragee.testing.Approval;
import io.dragee.testing.Compiler;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class RelationTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "relation");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "relation");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("DrageeOne.java"),
                SOURCE_FOLDER.resolve("DrageeTwo.java"),
                SOURCE_FOLDER.resolve("DrageeThree.java"),
                SOURCE_FOLDER.resolve("DrageeFour.java"),
                SOURCE_FOLDER.resolve("DrageeFive.java"),
                SOURCE_FOLDER.resolve("DrageeSix.java")
        );

        return compiler.executeProcessor();
    }

    private static String contentOfDragee(Compiler.Result actualResult) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("DrageeOne.json"));
    }

    @Test
    void dragee_matches_approval_one() {
        Compiler.Result actualResult = executeProcessor();

        String actualContent = contentOfDragee(actualResult);
        String expectedContent = Approval.readFileContent(Path.of("dragee_one.json"));

        assertThat(actualResult.success())
                .isTrue();

        assertThatJson(actualContent)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedContent);
    }

    @DisplayName("Dragee name is based on object fullName")
    @Nested
    class Name {
        @Test
        void name_of_dragee() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.name")
                    .isString()
                    .isEqualTo("io.dragee.rules.relation.DrageeOne");
        }
    }

    @DisplayName("Dragee has a kind based on annotations")
    @Nested
    class Kind {

        @Test
        void kind_of_dragee() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.kind_of")
                    .isString()
                    .isEqualTo("testing/test_object");
        }

    }

    @DisplayName("Relation between two dragees can exist through fields")
    @Nested
    class FieldRelation {
        @Test
        void direct_object() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeTwo']")
                    .isArray()
                    .contains("field");
        }

        @Test
        void generic_type() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeFive']")
                    .isArray()
                    .contains("field");
        }
    }

    @DisplayName("Relation between two dragees can exist through constructor")
    @Nested
    class ConstructorRelation {
        @Test
        void direct_object() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeTwo']")
                    .isArray()
                    .contains("constructor");
        }

        @Test
        void generic_type() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeThree']")
                    .isArray()
                    .contains("constructor");
        }
    }

    @DisplayName("Relation between two dragees can exist through method parameters")
    @Nested
    class MethodParamRelation {
        @Test
        void direct_object() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeTwo']")
                    .isArray()
                    .contains("method_param");
        }

        @Test
        void generic_type() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeThree']")
                    .isArray()
                    .contains("method_param");
        }

        @Test
        void is_omitted_when_method_is_private() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeSeven']")
                    .isAbsent();
        }
    }

    @DisplayName("Relation between two dragees can exist through method return")
    @Nested
    class MethodReturnRelation {
        @Test
        void direct_object() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeFour']")
                    .isArray()
                    .contains("method_return");
        }

        @Test
        void generic_type() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeFive']")
                    .isArray()
                    .contains("method_return");
        }

        @Test
        void is_omitted_when_method_is_private() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on.['io.dragee.rules.relation.DrageeSix']")
                    .isAbsent();
        }
    }

    @DisplayName("Relation with non-dragee objects are not kept")
    @Nested
    class NonDrageeTest {

        @Test
        void only_dragee_are_kept() {
            Compiler.Result actualResult = executeProcessor();

            String actualContent = contentOfDragee(actualResult);

            assertThatJson(actualContent)
                    .inPath("$.depends_on")
                    .isObject()
                    .containsOnlyKeys(
                            "io.dragee.rules.relation.DrageeTwo",
                            "io.dragee.rules.relation.DrageeThree",
                            "io.dragee.rules.relation.DrageeFour",
                            "io.dragee.rules.relation.DrageeFive");
        }
    }
}
