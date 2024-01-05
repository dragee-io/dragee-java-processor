package io.dragee.processor;

import io.dragee.annotation.Dragee;
import io.dragee.util.SimpleName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.dragee.util.SnakeCase.toSnakeCase;

class DrageeAnnotation {

    private final TypeElement element;
    private final io.dragee.annotation.Dragee dragee;

    private DrageeAnnotation(TypeElement element, Dragee dragee) {
        this.element = element;
        this.dragee = dragee;
    }

    public TypeElement element() {
        return element;
    }

    public String name() {
        String annotationName = element.getQualifiedName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public String kind() {
        return String.join("/", dragee.namespace(), name());
    }

    public boolean isPresentOn(Element element, Types types) {
        Set<Element> inheritedElements = inheritedElements(element.asType(), types);
        Set<Element> allAnnotationsOnElement = inheritedAnnotations(inheritedElements);

        return allAnnotationsOnElement.stream()
                .anyMatch(this.element::equals);
    }

    private Set<Element> inheritedElements(TypeMirror typeMirror, Types types) {
        return inheritedElements(typeMirror, types, new HashSet<>());
    }

    private Set<Element> inheritedElements(TypeMirror currentType, Types types, Set<Element> listToFill) {
        listToFill.add(types.asElement(currentType));

        types.directSupertypes(currentType)
                .forEach(type -> inheritedElements(type, types, listToFill));

        return listToFill;
    }

    private Set<Element> inheritedAnnotations(Set<Element> inheritedTypes) {
        return inheritedTypes.stream()
                .map(Element::getAnnotationMirrors)
                .flatMap(Collection::stream)
                .map(annotationMirror -> annotationMirror.getAnnotationType().asElement())
                .collect(Collectors.toSet());
    }

    static Optional<DrageeAnnotation> test(TypeElement annotationElement) {
        return lookupForDrageeMainAnnotation(annotationElement)
                .map(foundDragee -> new DrageeAnnotation(annotationElement, foundDragee));
    }

    private static Optional<io.dragee.annotation.Dragee> lookupForDrageeMainAnnotation(Element element) {
        return lookupForDrageeMainAnnotation(element, new ArrayList<>());
    }

    private static Optional<io.dragee.annotation.Dragee> lookupForDrageeMainAnnotation(Element element,
                                                                                       List<Element> alreadyLookedUp) {
        io.dragee.annotation.Dragee annotation = element.getAnnotation(io.dragee.annotation.Dragee.class);

        if (annotation != null) {
            return Optional.of(annotation);
        }

        alreadyLookedUp.add(element);

        return element.getAnnotationMirrors().stream()
                .map(annotationMirror -> annotationMirror.getAnnotationType().asElement())
                .filter(el -> !alreadyLookedUp.contains(el))
                .map(el -> DrageeAnnotation.lookupForDrageeMainAnnotation(el, alreadyLookedUp))
                .flatMap(Optional::stream)
                .findFirst();
    }

}
