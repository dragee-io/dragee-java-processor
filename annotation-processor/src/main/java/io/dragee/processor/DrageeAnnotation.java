package io.dragee.processor;

import io.dragee.model.Kind;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeAnnotation {

    private final DrageeNamespace namespace;
    private final DrageeName name;

    DrageeAnnotation(DrageeNamespace namespace, DrageeName name) {
        this.namespace = namespace;
        this.name = name;
    }

    public String kind() {
        return String.join(Kind.SEGMENT, namespace.toString(), name.toString());
    }

    public boolean isPresentOrInheritedOn(Element element, Types types) {
        Set<Element> inheritedElements = inheritedElements(element.asType(), types);
        Set<Element> allAnnotationsOnElement = inheritedAnnotations(inheritedElements);

        return allAnnotationsOnElement.stream()
                .anyMatch(this.name::isBasedOn);
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

}
