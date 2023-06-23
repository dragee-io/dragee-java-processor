package io.dragee.processor;

import com.google.auto.service.AutoService;
import io.dragee.model.Dragee;
import io.dragee.serializer.DrageeSerializer;
import io.dragee.serializer.JacksonDrageeSerializer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("io.dragee.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private final DrageeFactory drageeFactory = new DrageeFactory();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No annotation found. Stop process right there.");
            return false;
        }

        try {
            List<Dragee> dragees = drageeFactory.createDrajes(annotations, roundEnv);
            DrageeSerializer serializer = new JacksonDrageeSerializer(processingEnv.getFiler());
            serializer.serialize(dragees);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
