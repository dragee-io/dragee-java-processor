package io.dragee.rules.core;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DrageeTest {

    @Test
    void a_dragee_can_be_of_only_one_kind() throws IOException {
        // given
        Path sourcePath = Path.of("io", "dragee", "rules", "core", "sample", "MultipleKind.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(compiler::executeProcessor)
                .withCauseInstanceOf(DrageeCanNotBeOfMultipleKinds.class)
                .withMessageContaining("Dragee can not be of multiple kinds: 'io.dragee.rules.core.sample.MultipleKind', [kind_one, kind_two]");
    }

}
