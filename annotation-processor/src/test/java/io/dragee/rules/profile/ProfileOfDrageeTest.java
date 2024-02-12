package io.dragee.rules.profile;

import io.dragee.exception.WrongUsageOfProfiles;
import io.dragee.testing.Compiler;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProfileOfDrageeTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "profile");

    @Test
    void a_dragee_can_be_of_only_one_kind() {
        // given
        Path sourcePath = SOURCE_FOLDER.resolve("MultipleProfiles.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(compiler::executeProcessor)
                .withCauseInstanceOf(WrongUsageOfProfiles.class)
                .withMessageContaining("Dragee can not have more than one profile: 'io.dragee.rules.profile.MultipleProfiles', [testing/type_one, testing/type_two]");
    }

    @Test
    void one_valid_dragee() {
        // given
        Path sourcePath = SOURCE_FOLDER.resolve("ObjectOne.java");
        Compiler compiler = Compiler.compileTestClasses(sourcePath);

        // when
        assertThatCode(compiler::executeProcessor)
                .doesNotThrowAnyException();
    }

    @Test
    void two_profile_dragees() {
        // given
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("ObjectOne.java"),
                SOURCE_FOLDER.resolve("ObjectTwo.java")
        );

        // when
        assertThatCode(compiler::executeProcessor)
                .doesNotThrowAnyException();
    }

}
