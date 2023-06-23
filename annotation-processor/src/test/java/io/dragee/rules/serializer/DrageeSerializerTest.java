package io.dragee.rules.serializer;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DrageeSerializerTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "sample");

    @Test
    void serialize_object_to_dragee() {
        // given
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("DrageeOne.java"),
                SOURCE_FOLDER.resolve("DrageeTwo.java"),
                SOURCE_FOLDER.resolve("DrageeThree.java"),
                SOURCE_FOLDER.resolve("DrageeFour.java"),
                SOURCE_FOLDER.resolve("DrageeFive.java")
        );

        // when
        Compiler.Process actualProcess = compiler.executeProcessor();

        // then
        assertThat(actualProcess.success()).isTrue();

        // fixme: approval testing between files
    }

}
