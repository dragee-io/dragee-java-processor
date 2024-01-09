package io.dragee.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.dragee.exception.SerializationFailed;
import io.dragee.model.Dragee;
import lombok.Builder;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JacksonDrageeSerializer implements DrageeSerializer {

    private static final String PACKAGE_SEGMENT_SYMBOL = "\\.";
    private static final String PATH_SEGMENT_SYMBOL = "\\/";
    private static final String FILE_SUFFIX = ".json";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    private final String DRAGEE_FOLDER = "dragee";
    private final Filer filer;

    public JacksonDrageeSerializer(Filer filer) {
        this.filer = filer;
    }

    @Override
    public void serialize(Collection<Dragee> dragees) throws SerializationFailed {
        try {
            Path buildPath = rootOfBuildDirectory();
            Path drageeFolder = Files.createDirectories(buildPath.resolve(DRAGEE_FOLDER));

            for (Dragee dragee : dragees) {
                Path drageePath = pathOfDragee(dragee);
                Path drageeFullPath = Files.createDirectories(drageeFolder.resolve(drageePath));
                Path resultPath = drageeFullPath.resolve(dragee.shortName() + FILE_SUFFIX);

                SerializableDragee serializableDragee = SerializableDragee.from(dragee);
                objectMapper.writeValue(resultPath.toFile(), serializableDragee);
            }
        } catch (Exception e) {
            throw new SerializationFailed(e);
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

    @Builder
    private record SerializableDragee(String name, String kindOf, Map<String, Set<String>> dependsOn) {

        public static SerializableDragee from(Dragee dragee) {
            return SerializableDragee.builder()
                    .name(dragee.name())
                    .kindOf(dragee.kindOf())
                    .dependsOn(dragee.dependsOn().stream()
                            .map(dependency -> Map.entry(dependency.otherDragee(), dependency.types().stream()
                                    .map(type -> type.name().toLowerCase())
                                    .collect(Collectors.toSet())))
                            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue)))
                    .build();
        }

    }

}
