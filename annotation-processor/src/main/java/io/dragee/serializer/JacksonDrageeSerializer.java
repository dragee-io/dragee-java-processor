package io.dragee.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dragee.model.Dragee;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class JacksonDrageeSerializer implements DrageeSerializer {

    private static final String PACKAGE_SEGMENT_SYMBOL = "\\.";
    private static final String PATH_SEGMENT_SYMBOL = "\\/";
    private static final String FILE_SUFFIX = ".dragee";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String DRAGEE_FOLDER = "dragee";
    private final Filer filer;

    public JacksonDrageeSerializer(Filer filer) {
        this.filer = filer;
    }

    @Override
    public void serialize(List<Dragee> dragees) throws IOException {
        Path buildPath = rootOfBuildDirectory();
        Path drageeFolder = Files.createDirectories(buildPath.resolve(DRAGEE_FOLDER));

        for (Dragee dragee : dragees) {
            Path drageePath = pathOfDragee(dragee);
            Path drageeFullPath = Files.createDirectories(drageeFolder.resolve(drageePath));
            Path resultPath = drageeFullPath.resolve(dragee.shortName() + FILE_SUFFIX);
            objectMapper.writeValue(resultPath.toFile(), dragee);
        }
    }

    private Path rootOfBuildDirectory() throws IOException {
        FileObject fakeFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", DRAGEE_FOLDER);
        URI classOutputURI = fakeFile.toUri();
        return Path.of(classOutputURI).getParent().getParent();
    }

    private static Path pathOfDragee(Dragee dragee) {
        String pathOfDragee = dragee.namespace().replaceAll(PACKAGE_SEGMENT_SYMBOL, PATH_SEGMENT_SYMBOL);
        return Path.of(pathOfDragee);
    }

}
