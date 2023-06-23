package io.dragee.rules.serializer;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class DrageeSerializerTest {

    @Test
    void serialize_object_to_dragee() throws IOException {
        // given
        Compiler compiler = Compiler.compileTestClasses(
                Path.of("io", "dragee", "rules", "sample", "DrageeOne.java"),
                Path.of("io", "dragee", "rules", "sample", "DrageeTwo.java"),
                Path.of("io", "dragee", "rules", "sample", "DrageeThree.java"),
                Path.of("io", "dragee", "rules", "sample", "DrageeFour.java"),
                Path.of("io", "dragee", "rules", "sample", "DrageeFive.java")
        );

        // when
        Compiler.Process actualProcess = compiler.executeProcessor();

        // then
        assertThat(actualProcess.success()).isTrue();

        // fixme: approval testing between files
    }

}
