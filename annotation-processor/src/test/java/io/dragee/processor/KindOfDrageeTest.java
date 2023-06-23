package io.dragee.processor;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class KindOfDrageeTest {

    @Test
    void a_dragee_can_be_of_only_one_kind() throws IOException {
        // given
        Path sourcePath = Path.of("io", "dragee", "sample", "rules", "kind", "MultipleKind.java");
        JavaCompiler.CompilationTask task = ProcessorTest.prepareTaskFor(sourcePath);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(task::call)
                .withCauseInstanceOf(DrageeCanNotBeOfMultipleKinds.class)
                .withMessageContaining("Dragee can not be of multiple kinds: 'io.dragee.sample.rules.kind.MultipleKind', [aggregate,value_object]");
    }

}
