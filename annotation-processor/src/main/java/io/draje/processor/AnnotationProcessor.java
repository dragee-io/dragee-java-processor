package io.draje.processor;

import com.google.auto.service.AutoService;
import io.draje.model.Draje;
import io.draje.serializer.DrajeSerializer;
import io.draje.serializer.JacksonDrajeSerializer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private final DrajeFactory drajeFactory = new DrajeFactory();
    private final DrajeSerializer serializer = new JacksonDrajeSerializer();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        try {
            List<Draje> drajes = drajeFactory.createDrajes(annotations, roundEnv);
            serializer.serialize(drajes);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return processingEnv.getElementUtils().getPackageElement("io.draje.annotation")
                .getEnclosedElements()
                .stream()
                .map(enclosedElement -> enclosedElement.asType().toString())
                .collect(Collectors.toSet());
    }
}
