package io.dragee.rules.sub_namespace;

import io.dragee.exception.WrongUsageOfNamespaces;
import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DrageeInvalidSubNamespaceTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "sub_namespace");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("AnOtherObject.java")
        );

        return compiler.executeProcessor();
    }

    @Test
    void sub_namespace_can_not_come_from_more_than_one_other_namespace() {
        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(DrageeInvalidSubNamespaceTest::executeProcessor)
                .withCauseInstanceOf(WrongUsageOfNamespaces.class)
                .withMessageContaining("A sub namespace can not depend on more than one other namespace: ['SubFromTwoNamespaces']");
    }

}
