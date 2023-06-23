package io.dragee.rules.ddd;

import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static io.dragee.testing.AssertThatDragee.assertThatDragees;
import static org.assertj.core.api.Assertions.assertThat;

class DDDProcessorTest {

    @Test
    void execute_processor() throws IOException {
        // given
        Path samplePath = Path.of("io", "dragee", "rules", "ddd", "sample");
        Compiler compiler = Compiler.compileTestClasses(samplePath);

        // when
        Compiler.Process actualProcess = compiler.executeProcessor();

        // then
        assertThat(actualProcess.isSuccess()).isTrue();

        assertThatDragees(
                "OneAggregate",
                "OneCommand",
                "OneEvent",
                "OneValue",
                "OneFactory",
                "OneRepository",
                "OneService"
        ).existsIn(actualProcess.drageesFolder());

    }
}