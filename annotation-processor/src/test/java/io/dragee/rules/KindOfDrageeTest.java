package io.dragee.rules;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class KindOfDrageeTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "sample", "kind");

    @Test
    void a_dragee_can_be_of_only_one_kind() {
        // given
        Path sourcePath = SOURCE_FOLDER.resolve("MultipleKind.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(compiler::executeProcessor)
                .withCauseInstanceOf(DrageeCanNotBeOfMultipleKinds.class)
                .withMessageContaining("Dragee can not be of multiple kinds: 'io.dragee.rules.sample.kind.MultipleKind', [kind_one, kind_two]");
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
