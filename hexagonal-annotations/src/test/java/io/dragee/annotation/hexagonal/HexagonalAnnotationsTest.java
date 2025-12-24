package io.dragee.annotation.hexagonal;

import io.dragee.testing.Approval;
import io.dragee.testing.Compiler;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class HexagonalAnnotationsTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "annotation", "hexagonal", "sample");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "annotation", "hexagonal", "sample");

    private static Compiler.Result executeProcessor() {
        Compiler compiler = Compiler.compileTestClasses(
                SOURCE_FOLDER.resolve("ADomainService.java"),
                SOURCE_FOLDER.resolve("ADomainEntity.java"),
                SOURCE_FOLDER.resolve("AnInboundPort.java"),
                SOURCE_FOLDER.resolve("AnOutboundPort.java"),
                SOURCE_FOLDER.resolve("AnInboundAdapter.java"),
                SOURCE_FOLDER.resolve("AnOutboundAdapter.java")
        );

        return compiler.executeProcessor();
    }

    @ParameterizedTest
    @CsvSource({
            "ADomainService, domain_service",
            "ADomainEntity, domain_entity",
            "AnInboundPort, inbound_port",
            "AnOutboundPort, outbound_port",
            "AnInboundAdapter, inbound_adapter",
            "AnOutboundAdapter, outbound_adapter",
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
