package io.dragee.rules.serializer;

import io.dragee.testing.Approval;
import io.dragee.testing.Compiler;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

public class DrageeSerializerTest {

    private static final Path SOURCE_FOLDER = Path.of("io", "dragee", "rules", "sample");
    private static final Path OUTPUT_FOLDER = Path.of("io", "dragee", "rules", "sample");

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
        Compiler.Result actualResult = compiler.executeProcessor();

        // then
        String actualContent = actualResult.readDrageeFile(OUTPUT_FOLDER.resolve("DrageeOne.json"));
        String expectedContent = Approval.readFileContent(Path.of("dragee_one.json"));

        assertThat(actualResult.success())
                .isTrue();

        assertThatJson(actualContent)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedContent);
    }

}
