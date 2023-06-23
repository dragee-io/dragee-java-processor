package io.draje.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.draje.model.Draje;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JacksonDrajeSerializer implements DrajeSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String FILENAME = "drajes.json";
    private final String RESULT_FOLDER = "draje";

    @Override
    public void serialize(List<Draje> drajes) throws IOException {
        try {
            URI targetURI = getClass().getResource("/").toURI();
            Path targetPath = Paths.get(targetURI).getParent();
            Path resultFolder = Files.createDirectories(targetPath.resolve(RESULT_FOLDER));
            Path resultPath = resultFolder.resolve(FILENAME);

            objectMapper.writeValue(resultPath.toFile(), drajes);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
