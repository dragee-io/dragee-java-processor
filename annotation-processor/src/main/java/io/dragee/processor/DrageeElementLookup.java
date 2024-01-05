package io.dragee.processor;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeElementLookup {

    private final Types types;

    DrageeElementLookup(Types types) {
        this.types = types;
    }

    public DrageeElements findCandidates(DrageeAnnotations annotations,  RoundEnvironment roundEnv) {
        TypeElement[] typeElements = annotations.asTypeElements();
        Set<? extends Element> elements = getElementsAnnotatedWithAny(typeElements, roundEnv);

        Set<DrageeElement> drageeElements = elements.stream()
                .map(element -> toDrageeElement(annotations, element))
                .collect(Collectors.toSet());

        return DrageeElements.from(drageeElements);
    }

    private Set<? extends Element> getElementsAnnotatedWithAny(TypeElement[] typeElements, RoundEnvironment roundEnv) {
        Set<? extends Element> rootElements = roundEnv.getRootElements();
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWithAny(typeElements);

        return annotatedElements.stream()
                .map(Element::asType)
                .map(foundType -> lookupForInheritance(foundType, rootElements))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<? extends Element> lookupForInheritance(TypeMirror foundType, Set<? extends Element> rootElements) {
        return rootElements.stream()
                .filter(element -> types.isAssignable(element.asType(), foundType))
                .collect(Collectors.toSet());
    }

    private DrageeElement toDrageeElement(DrageeAnnotations drageeAnnotations, Element element) {
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

        DrageeAnnotation drageeAnnotation = drageeAnnotationsOnElement.iterator().next();
        return new DrageeElement(element, DrageeElement.Kind.of(drageeAnnotation.kind()));
    }

}
