package io.dragee.processor;

import io.dragee.annotation.Dragee;
import io.dragee.util.SimpleName;

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
    private final Element namespace;

    private DrageeAnnotation(TypeElement element, Element namespace) {
        this.element = element;
        this.namespace = namespace;
    }

    public String name() {
        String annotationName = element.getQualifiedName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public String namespace() {
        String annotationName = namespace.getSimpleName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public String kind() {
        return String.join("/", namespace(), name());
    }

    public boolean isPresentOrInheritedOn(Element element, Types types) {
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
        return lookupForNamespace(annotationElement)
                .map(foundDragee -> new DrageeAnnotation(annotationElement, foundDragee));
    }

    private static Optional<Element> lookupForNamespace(Element element) {
        return lookupForNamespace(element, new ArrayList<>());
    }

    private static Optional<Element> lookupForNamespace(Element element,
                                                        List<Element> alreadyLookedUp) {
        Dragee.Namespace annotation = element.getAnnotation(Dragee.Namespace.class);

        if (annotation != null) {
            return Optional.of(element);
        }

        alreadyLookedUp.add(element);

        return element.getAnnotationMirrors().stream()
                .map(annotationMirror -> annotationMirror.getAnnotationType().asElement())
                .filter(el -> !alreadyLookedUp.contains(el))
                .map(el -> DrageeAnnotation.lookupForNamespace(el, alreadyLookedUp))
                .flatMap(Optional::stream)
                .findFirst();
    }

}
