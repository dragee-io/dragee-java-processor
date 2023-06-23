package io.dragee.processor;

import io.dragee.model.Dragee;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DrageeFactory {

    public List<Dragee> createDrajes(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<DrageeElement> drageeElements = findDrageeElements(annotations, roundEnv);
        return createDrajes(drageeElements);
    }

    private static List<DrageeElement> findDrageeElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends TypeElement> drageeAnnotations = findDrageeAnnotations(annotations);
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWithAny(drageeAnnotations.toArray(new TypeElement[]{}));

        return annotatedElements.stream()
                .map(element -> {
                    Set<? extends TypeElement> drageeAnnotationsOnElement = drageeAnnotations.stream()
                            .filter(drageeAnnotation -> element.getAnnotationMirrors().stream()
                                    .anyMatch(annotationMirror -> annotationMirror.getAnnotationType().equals(drageeAnnotation.asType())))
                            .collect(Collectors.toSet());

                    return new DrageeElement(element, drageeAnnotationsOnElement);
                })
                .toList();
    }

    private static Set<? extends TypeElement> findDrageeAnnotations(Set<? extends TypeElement> annotations) {
        return annotations.stream()
                .filter(annotation -> annotation.getAnnotation(io.dragee.annotation.Dragee.class) != null)
                .collect(Collectors.toSet());
    }

    private List<Dragee> createDrajes(List<DrageeElement> drageeElements) {
        return drageeElements.stream()
                .map(drageeElement -> Dragee.builder()
                        .name(drageeElement.name())
                        .kindOf(drageeElement.kind())
                        .constructors(drageeElement.constructors())
                        .fields(drageeElement.fields())
                        .methods(drageeElement.methods())
                        .build())
                .toList();
    }

}
