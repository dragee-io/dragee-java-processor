package io.dragee.testing;

import io.dragee.processor.AnnotationProcessor;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Compiler {

    private final Path compilationFolder;
    private final Path sourceOutputFolder;
    private final Path classOutputFolder;
    private final JavaCompiler.CompilationTask task;

    private Compiler(List<Path> sourcePaths) throws IOException {
        this.compilationFolder = Files.createTempDirectory("dragee");
        this.sourceOutputFolder = Files.createDirectories(compilationFolder.resolve("sources"));
        this.classOutputFolder = Files.createDirectories(compilationFolder.resolve("classes"));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocationFromPaths(StandardLocation.SOURCE_OUTPUT, List.of(sourceOutputFolder));
        fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(classOutputFolder));

        List<Path> javaFilePaths = walkThroughFiles(sourcePaths);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(javaFilePaths);

        this.task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        this.task.setProcessors(List.of(new AnnotationProcessor()));
    }

    public Process executeProcessor() {
        return new Process().execute();
    }

    public static Compiler compileTestClasses(Path... sourcePaths) throws IOException {
        List<Path> testSourcePaths = List.of(sourcePaths).stream()
                .map(path -> Path.of("src", "test", "java").resolve(path))
                .toList();

        return new Compiler(testSourcePaths);
    }

    private static List<Path> walkThroughFiles(List<Path> sourcePaths) {
        return sourcePaths.stream()
                .flatMap(sourceObject -> {
                    try {
                        return Files.walk(sourceObject);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Files::isRegularFile)
                .toList();
    }

    public class Process {

        private final Path drageeFolder = compilationFolder.resolve("dragee");
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public Path drageesFolder() {
            return drageeFolder;
        }

        public Process execute() {
            this.success = task.call();
            return this;
        }

    }
}
