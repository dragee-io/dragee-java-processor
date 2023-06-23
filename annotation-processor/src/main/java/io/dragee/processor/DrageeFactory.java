package io.dragee.processor;

import io.dragee.model.Dragee;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DrageeFactory {

    public List<io.dragee.model.Dragee> createDrajes(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends TypeElement> drageeAnnotations = findDrageeAnnotations(annotations);
        List<DrageeElement> drageeElements = getDrageeElements(drageeAnnotations, roundEnv);
        return createDrajes(drageeElements);
    }

    private static Set<? extends TypeElement> findDrageeAnnotations(Set<? extends TypeElement> annotations) {
        return annotations.stream()
                .filter(annotation -> annotation.getAnnotation(io.dragee.annotation.Dragee.class) != null)
                .collect(Collectors.toSet());
    }

    private static List<DrageeElement> getDrageeElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<Element, Set<TypeElement>> annotationsPerElement = new HashMap<>();

        annotations.forEach(annotation -> roundEnv.getElementsAnnotatedWith(annotation)
                .forEach(element -> annotationsPerElement.computeIfAbsent(element, (key) -> new HashSet<>())
                        .add(annotation)));

        return annotationsPerElement.entrySet().stream()
                .map(entry -> new DrageeElement(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<io.dragee.model.Dragee> createDrajes(List<DrageeElement> drageeElements) {
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
