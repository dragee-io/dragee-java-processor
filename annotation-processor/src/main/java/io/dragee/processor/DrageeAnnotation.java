package io.dragee.processor;

import io.dragee.util.SimpleName;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.dragee.util.SnakeCase.toSnakeCase;

record DrageeAnnotation(TypeElement annotation) {

    public String kind() {
        String annotationName = annotation.getQualifiedName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public boolean isPresentOn(Element element, Types types) {
        Set<Element> inheritedElements = inheritedElements(element.asType(), types);
        Set<Element> allAnnotationsOnElement = inheritedAnnotations(inheritedElements);

        return allAnnotationsOnElement.stream()
                .anyMatch(annotation::equals);
    }

    private static Set<Element> inheritedElements(TypeMirror typeMirror, Types types) {
        return inheritedElements(typeMirror, types, new HashSet<>());
    }

    private static Set<Element> inheritedElements(TypeMirror currentType, Types types, Set<Element> listToFill) {
        listToFill.add(types.asElement(currentType));

        types.directSupertypes(currentType)
                .forEach(type -> inheritedElements(type, types, listToFill));

        return listToFill;
    }

    private static Set<Element> inheritedAnnotations(Set<Element> inheritedTypes) {
        return inheritedTypes.stream()
                .map(Element::getAnnotationMirrors)
                .flatMap(Collection::stream)
                .map(annotationMirror -> annotationMirror.getAnnotationType().asElement())
                .collect(Collectors.toSet());
    }

}
