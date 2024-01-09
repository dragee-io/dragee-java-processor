package io.dragee.annotation.ddd;

import io.dragee.testing.Approval;
import io.dragee.testing.Compiler;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class DDDAnnotationsTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "annotation", "ddd", "sample");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "annotation", "ddd", "sample");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("AnAggregate.java"),
                SOURCE_FOLDER.resolve("AnEntity.java"),
                SOURCE_FOLDER.resolve("AnEvent.java"),
                SOURCE_FOLDER.resolve("ACommand.java"),
                SOURCE_FOLDER.resolve("AFactory.java"),
                SOURCE_FOLDER.resolve("AService.java"),
                SOURCE_FOLDER.resolve("ARepository.java"),
                SOURCE_FOLDER.resolve("AValueObject.java")
        );

        return compiler.executeProcessor();
    }

    private static String contentOfDragee(Compiler.Result actualResult, String expectedFileName) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve(jsonExtension(expectedFileName)));
    }

    @ParameterizedTest
    @CsvSource({
            "AnAggregate, aggregate",
            "AnEntity, entity",
            "AnEvent, event",
            "ACommand, command",
            "AFactory, factory",
            "AService, service",
            "ARepository, repository",
            "AValueObject, value_object",
    })
    void dragee_matches_approval_one(String expectedFileName, String approvalFileName) {
        Compiler.Result actualResult = executeProcessor();

        String actualContent = contentOfDragee(actualResult, expectedFileName);
        String expectedContent = Approval.readFileContent(Path.of(jsonExtension(approvalFileName)));

        assertThat(actualResult.success())
                .isTrue();

        assertThatJson(actualContent)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedContent);
    }

    private static String jsonExtension(String fileName) {
        return fileName + ".json";
    }
}
