package io.dragee.processor;

import io.dragee.annotation.Dragee;
import io.dragee.util.SimpleName;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

import static io.dragee.util.SnakeCase.toSnakeCase;

class DrageeAnnotation {

    private final TypeElement annotation;
    private final Element namespace;

    private DrageeAnnotation(TypeElement annotation, Element namespace) {
        this.annotation = annotation;
        this.namespace = namespace;
    }

    public String name() {
        String annotationName = annotation.getQualifiedName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public String namespaceFullName(){
        return namespace.toString();
    }
    public String namespaceSimpleName() {
        String annotationName = namespace.getSimpleName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public String kind() {
        return String.join("/", namespaceSimpleName(), name());
    }

    public boolean isPresentOrInheritedOn(Element element, Types types) {
        Set<Element> inheritedElements = inheritedElements(element.asType(), types);
        Set<Element> allAnnotationsOnElement = inheritedAnnotations(inheritedElements);

        return allAnnotationsOnElement.stream()
                .anyMatch(this.annotation::equals);
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

    static Set<DrageeAnnotation> test(TypeElement annotationElement) {
        return lookupForNamespace(annotationElement)
                .stream()
                .map(foundDragee -> new DrageeAnnotation(annotationElement, foundDragee))
                .collect(Collectors.toSet());
    }

    private static Set<Element> lookupForNamespace(Element element) {
        var namespaces = new LinkedHashSet<Element>();
        lookupForNamespace(element,new ArrayList<>(), namespaces);
        return namespaces;
    }

    private static void lookupForNamespace(Element element,List<Element> alreadyLookedUp, Set<Element> namespaces) {
        Dragee.Namespace annotation = element.getAnnotation(Dragee.Namespace.class);

        if (annotation != null) {
            namespaces.add(element);
        }
        
        element.getAnnotationMirrors().stream()
                .map(annotationMirror -> annotationMirror.getAnnotationType().asElement())
                .filter(el -> !alreadyLookedUp.contains(el))
                .forEach(el -> {
                    alreadyLookedUp.add(element);
                    DrageeAnnotation.lookupForNamespace(el, alreadyLookedUp, namespaces);
                });
    }


}
