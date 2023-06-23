package io.dragee.processor;

import javax.tools.JavaCompiler;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProcessorTest {

    public static JavaCompiler.CompilationTask prepareTaskFor(Path... sourcePaths) throws IOException {
        List<Path> testSourcePaths = List.of(sourcePaths).stream()
                .map(path -> Path.of("src", "test", "java").resolve(path))
                .toList();

        JavaCompiler.CompilationTask task = Compiler.compile(testSourcePaths);
        task.setProcessors(List.of(new AnnotationProcessor()));
        return task;
    }

}
