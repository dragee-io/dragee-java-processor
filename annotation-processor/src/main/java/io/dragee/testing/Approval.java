package io.dragee.testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Approval {

    private static final Path APPROVAL_FOLDER = Path.of("src", "test", "resources", "approval");

    public static String readFileContent(Path approvalFile) {
        try {
            Path path = APPROVAL_FOLDER.resolve(approvalFile);
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to read approval file '%s'", approvalFile), e);
        }
    }

}
