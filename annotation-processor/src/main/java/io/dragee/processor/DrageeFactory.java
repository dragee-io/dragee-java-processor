package io.dragee.processor;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.model.Dragee;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DrageeFactory {

    private final Types types;

    public DrageeFactory(Types types) {
        this.types = types;
    }

    public List<Dragee> createDragees(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<DrageeElement> drageeElements = findDrageeElements(annotations, roundEnv);
        return toDragees(drageeElements);
    }

    private List<DrageeElement> findDrageeElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<DrageeAnnotation> drageeAnnotations = findDrageeAnnotations(annotations);
        Set<? extends Element> annotatedElements = getElementsAnnotatedWithAny(drageeAnnotations, roundEnv);

        return annotatedElements.stream()
                .map(element -> toDrageeElement(drageeAnnotations, element))
                .toList();
    }

    private DrageeElement toDrageeElement(Set<DrageeAnnotation> drageeAnnotations, Element element) {
        Set<DrageeAnnotation> drageeAnnotationsOnElement = drageeAnnotations.stream()
                .filter(drageeAnnotation -> drageeAnnotation.isPresentOn(element, types))
                .collect(Collectors.toSet());

        if (drageeAnnotationsOnElement.size() != 1) {
            List<String> kinds = drageeAnnotationsOnElement.stream()
                    .map(DrageeAnnotation::kind)
                    .sorted()
                    .toList();

            throw new DrageeCanNotBeOfMultipleKinds(element.toString(), kinds);
        }

        String kindOfDragee = drageeAnnotationsOnElement.iterator().next().kind();
        return new DrageeElement(element, DrageeElement.Kind.of(kindOfDragee));
    }

    private Set<? extends Element> getElementsAnnotatedWithAny(Set<DrageeAnnotation> drageeAnnotations, RoundEnvironment roundEnv) {
        TypeElement[] typeElements = drageeAnnotations.stream()
                .map(DrageeAnnotation::annotation)
                .toArray(TypeElement[]::new);

        Set<? extends Element> rootElements = roundEnv.getRootElements();
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWithAny(typeElements);

        return annotatedElements.stream()
                .map(Element::asType)
                .map(foundType -> rootElements.stream()
                        .filter(element -> types.isAssignable(element.asType(), foundType))
                        .collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static Set<DrageeAnnotation> findDrageeAnnotations(Set<? extends TypeElement> annotations) {
        return annotations.stream()
                .filter(annotation -> Objects.nonNull(annotation.getAnnotation(io.dragee.annotation.Dragee.class)))
                .map(DrageeAnnotation::new)
                .collect(Collectors.toSet());
    }

    private static List<Dragee> toDragees(List<DrageeElement> drageeElements) {
        return drageeElements.stream()
                .map(element -> toDragee(element, drageeElements))
                .toList();
    }

    private static Dragee toDragee(DrageeElement drageeElement, List<DrageeElement> drageeElements) {
        return Dragee.builder()
                .name(drageeElement.name())
                .kindOf(drageeElement.kind().value())
                .dependsOn(drageeElements.stream()
                        .map(drageeElement::findDependencyWith)
                        .flatMap(Optional::stream)
                        .collect(Collectors.toSet()))
                .build();
    }

}
