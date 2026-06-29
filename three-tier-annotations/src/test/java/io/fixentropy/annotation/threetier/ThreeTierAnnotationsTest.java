package io.fixentropy.annotation.threetier;

import io.fixentropy.testing.Approval;
import io.fixentropy.testing.Compiler;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class ThreeTierAnnotationsTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "fixentropy", "annotation", "threetier", "sample");
    private static final Path OUTPUT_FOLDER = Path.of("io", "fixentropy", "annotation", "threetier", "sample");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("AController.java"),
                SOURCE_FOLDER.resolve("AService.java"),
                SOURCE_FOLDER.resolve("ARepository.java"),
                SOURCE_FOLDER.resolve("AnEntity.java"),
                SOURCE_FOLDER.resolve("ARequestModel.java"),
                SOURCE_FOLDER.resolve("AResponseModel.java"),
                SOURCE_FOLDER.resolve("AMapper.java")
        );

        return compiler.executeProcessor();
    }

    @ParameterizedTest
    @CsvSource({
            "AController, controller",
            "AService, service",
            "ARepository, repository",
            "AnEntity, entity",
            "ARequestModel, request_model",
            "AResponseModel, response_model",
            "AMapper, mapper",
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

    private static String contentOfDragee(Compiler.Result actualResult, String expectedFileName) {
        return actualResult.readDrageeFile(OUTPUT_FOLDER.resolve(jsonExtension(expectedFileName)));
    }

    private static String jsonExtension(String fileName) {
        return fileName + ".json";
    }
}
