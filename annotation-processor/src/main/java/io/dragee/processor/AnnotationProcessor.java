package io.dragee.processor;

import com.google.auto.service.AutoService;
import io.dragee.model.Dragee;
import io.dragee.serializer.DrageeSerializer;
import io.dragee.serializer.JacksonDrageeSerializer;

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

    private final DrageeFactory drageeFactory = new DrageeFactory();
    private final DrageeSerializer serializer = new JacksonDrageeSerializer();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        try {
            List<Dragee> dragees = drageeFactory.createDrajes(annotations, roundEnv);
            serializer.serialize(dragees);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return processingEnv.getElementUtils().getPackageElement("io.dragee.annotation")
                .getEnclosedElements()
                .stream()
                .map(enclosedElement -> enclosedElement.asType().toString())
                .collect(Collectors.toSet());
    }
}
