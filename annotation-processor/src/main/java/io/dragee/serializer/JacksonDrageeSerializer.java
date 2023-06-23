package io.dragee.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dragee.model.Dragee;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JacksonDrageeSerializer implements DrageeSerializer {

    private static final String PACKAGE_SEGMENT_SYMBOL = "\\.";
    private static final String PATH_SEGMENT_SYMBOL = "\\/";
    private static final String FILE_SUFFIX = ".dragee";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String DRAGEE_FOLDER = "dragee";

    @Override
    public void serialize(List<Dragee> dragees) throws IOException {
        try {
            URI targetURI = getClass().getResource("/").toURI();
            Path targetPath = Paths.get(targetURI).getParent();
            Path drageeFolder = Files.createDirectories(targetPath.resolve(DRAGEE_FOLDER));

            for (Dragee dragee : dragees) {
                Path drageePath = pathOfDragee(dragee);
                Path drageeFullPath = Files.createDirectories(drageeFolder.resolve(drageePath));
                Path resultPath = drageeFullPath.resolve(dragee.shortName() + FILE_SUFFIX);
                objectMapper.writeValue(resultPath.toFile(), dragee);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path pathOfDragee(Dragee dragee) {
        String pathOfDragee = dragee.namespace().replaceAll(PACKAGE_SEGMENT_SYMBOL, PATH_SEGMENT_SYMBOL);
        return Path.of(pathOfDragee);
    }
}
