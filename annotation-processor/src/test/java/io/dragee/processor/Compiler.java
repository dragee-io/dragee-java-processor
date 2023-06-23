package io.dragee.processor;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Compiler {

    public static JavaCompiler.CompilationTask compile(List<Path> sourcePaths) throws IOException {
        List<Path> filePaths = walkThroughFiles(sourcePaths);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Path temporaryFolder = Files.createTempDirectory("draje");
        fileManager.setLocationFromPaths(StandardLocation.SOURCE_OUTPUT, List.of(temporaryFolder));
        fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(temporaryFolder));

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(filePaths);
        return compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
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

}
