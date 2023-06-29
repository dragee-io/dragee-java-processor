package io.dragee.processor;

import com.google.auto.service.AutoService;
import io.dragee.model.Dragee;
import io.dragee.serializer.DrageeSerializer;
import io.dragee.serializer.JacksonDrageeSerializer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private DrageeFactory drageeFactory;
    private DrageeSerializer drageeSerializer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.drageeFactory = new DrageeFactory(processingEnv.getTypeUtils());
        this.drageeSerializer = new JacksonDrageeSerializer(processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No annotation found. Stop process right there.");
            return false;
        }

        List<Dragee> dragees = drageeFactory.createDragees(annotations, roundEnv);
        drageeSerializer.serialize(dragees);
        return true;
    }
}
