package io.dragee.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeProfile {

    public static final String SEPARATOR = "/";

    private final DrageeNamespace namespace;
    private final DrageeType type;

    DrageeProfile(DrageeNamespace namespace, DrageeType type) {
        this.namespace = namespace;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.join(SEPARATOR, namespace.toString(), type.toString());
    }

    public boolean isPresentOrInheritedOn(Element element, Types types) {
        Set<Element> inheritedElements = inheritedElements(element, types);
        Set<Element> allAnnotationsOnElement = inheritedAnnotations(inheritedElements);

        return allAnnotationsOnElement.stream()
                .anyMatch(this.type::isBasedOn);
    }

    private Set<Element> inheritedElements(Element element, Types types) {
        if (this.type.isInheritable()) {
            return inheritedElements(element.asType(), types);
        }

        return Set.of(element);
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
