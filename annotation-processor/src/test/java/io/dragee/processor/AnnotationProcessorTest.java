package io.dragee.processor;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationProcessorTest {

    @Test
    void execute_processor() throws IOException {
        // given
        Path samplePath = Path.of("io", "dragee", "sample", "rules", "serializer");
        JavaCompiler.CompilationTask task = ProcessorTest.prepareTaskFor(samplePath);

        // when
        boolean success = task.call();

        // then
        assertThat(success).isTrue();
    }
}