package io.dragee.testing;

import org.assertj.core.api.AbstractAssert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertThatDragee extends AbstractAssert<AssertThatDragee, List<String>> {

    private static final String DRAGEE_EXTENSION = ".dragee";

    protected AssertThatDragee(String objectName) {
        super(List.of(objectName), AssertThatDragee.class);
    }

    protected AssertThatDragee(String... objectNames) {
        super(List.of(objectNames), AssertThatDragee.class);
    }

    public static AssertThatDragee assertThatDragee(String objectName) {
        return new AssertThatDragee(objectName);
    }

    public static AssertThatDragee assertThatDragees(String... objectName) {
        return new AssertThatDragee(objectName);
    }

    public AssertThatDragee existsIn(Path rootFolder) throws IOException {
        Set<Path> pathsOfDragees = Files.walk(rootFolder)
                .filter(Files::isRegularFile)
                .collect(Collectors.toSet());

        actual.forEach(drageeName -> {
            boolean exists = pathsOfDragees.stream()
                    .anyMatch(path -> path.endsWith(drageeName + DRAGEE_EXTENSION));

            assertThat(exists).isTrue();
        });

        return this;
    }

}
