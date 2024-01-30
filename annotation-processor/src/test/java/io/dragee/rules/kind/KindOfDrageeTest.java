package io.dragee.rules.kind;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class KindOfDrageeTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "kind");

    @Test
    void a_dragee_can_be_of_only_one_kind_if_subnamespaces_are_different() {
        // given
        Path sourcePath = SOURCE_FOLDER.resolve("MultipleKind.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(compiler::executeProcessor)
                .withCauseInstanceOf(DrageeCanNotBeOfMultipleKinds.class)
                .withMessageContaining("Dragee can not be of multiple kinds: 'io.dragee.rules.kind.MultipleKind', [testing/kind_one, testing/kind_two]");
    }


    @Test
    void a_dragee_can_have_two_annotations_if_sub_namespace_is_present() {
        Path sourcePath = SOURCE_FOLDER.resolve("MultipleSubNamespace.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        assertThatCode(compiler::executeProcessor)
                .doesNotThrowAnyException();

    }

    @Test
    void a_dragee_cant_have_two_different_namespaces() {
        Path sourcePath = SOURCE_FOLDER.resolve("MultipleDifferentNamespaces.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);


        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(compiler::executeProcessor)
                .withCauseInstanceOf(DrageeCanNotBeOfMultipleKinds.class)
                .withMessageContaining("Dragee can not be of multiple kinds: 'io.dragee.rules.kind.MultipleDifferentNamespaces'")
                .withMessageContaining("some_other_namespace/some_other_concept")
                .withMessageContaining("some_namespace/some_concept")
                .withMessageContaining("some_sub_namespace/some_concept")
        ;

    }

    @Test
    void one_valid_dragee() {
        // given
        Path sourcePath = SOURCE_FOLDER.resolve("ObjectOne.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        // when
        assertThatCode(compiler::executeProcessor)
                .doesNotThrowAnyException();
    }

    @Test
    void two_kind_of_dragees() {
        // given
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("ObjectOne.java"),
                SOURCE_FOLDER.resolve("ObjectTwo.java")
        );

        // when
        assertThatCode(compiler::executeProcessor)
                .doesNotThrowAnyException();
    }

}
