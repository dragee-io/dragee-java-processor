package io.draje.processor;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationProcessorTest {

    @Test
    void execute_processor() throws IOException {
        // given
        List<Path> pathsToScan = List.of(Path.of("src", "test", "java", "io", "draje", "sample"));
        JavaCompiler.CompilationTask task = Compiler.compile(pathsToScan);
        task.setProcessors(List.of(new AnnotationProcessor()));

        // when
        boolean success = task.call();

        // then
        assertThat(success).isTrue();
    }
}