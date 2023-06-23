package io.dragee.processor;

import io.dragee.annotation.KindOf;
import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.model.Constructor;
import io.dragee.model.Dragee;
import io.dragee.model.Field;
import io.dragee.model.Method;
import io.dragee.model.Parameter;
import io.dragee.model.Return;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrageeFactory {

    public List<Dragee> createDrajes(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<AnnotatedElement> annotatedElements = getElementsWithTheirAnnotations(annotations, roundEnv);

        return createDrajes(annotatedElements);
    }

    private static List<AnnotatedElement> getElementsWithTheirAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<Element, Set<TypeElement>> annotationsPerElement = new HashMap<>();

        annotations.forEach(annotation -> roundEnv.getElementsAnnotatedWith(annotation)
                .forEach(element -> annotationsPerElement.computeIfAbsent(element, (key) -> new HashSet<>())
                        .add(annotation)));

        return annotationsPerElement.entrySet().stream()
                .map(entry -> new AnnotatedElement(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<Dragee> createDrajes(List<AnnotatedElement> annotatedElements) {
        return annotatedElements.stream()
                .map(annotatedElement -> Dragee.builder()
                        .name(nameOf(annotatedElement))
                        .kindOf(kindOf(annotatedElement))
                        .constructors(constructorsOf(annotatedElement.element))
                        .fields(fieldsOf(annotatedElement.element))
                        .methods(methodsOf(annotatedElement.element))
                        .build())
                .toList();
    }

    private static String nameOf(AnnotatedElement annotatedElement) {
        return annotatedElement.element.toString();
    }

    private static String kindOf(AnnotatedElement annotatedElement) {
        List<String> kinds = annotatedElement.annotations.stream()
                .flatMap(annotation -> Stream.ofNullable(annotation.getAnnotation(KindOf.class)))
                .map(KindOf::value)
                .sorted()
                .toList();

        if (kinds.size() != 1) {
            throw new DrageeCanNotBeOfMultipleKinds(nameOf(annotatedElement), kinds);
        }

        return kinds.get(0);
    }

    private static List<Constructor> constructorsOf(Element element) {
        return element.getEnclosedElements().stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.CONSTRUCTOR)
                .map(enclosedElement -> Constructor.builder()
                        .parameters(parametersOf((ExecutableElement) enclosedElement))
                        .build())
                .toList();
    }

    private static List<Field> fieldsOf(Element element) {
        return element.getEnclosedElements().stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .map(enclosedElement -> Field.builder()
                        .type(enclosedElement.asType().toString())
                        .name(enclosedElement.toString())
                        .build())
                .toList();
    }

    private static List<Parameter> parametersOf(ExecutableElement element) {
        return element.getParameters().stream()
                .map(parameter -> Parameter.builder()
                        .type(parameter.asType().toString())
                        .name(parameter.toString())
                        .build())
                .toList();
    }

    private static Return returnOf(ExecutableElement element) {
        return Return.builder()
                .type(element.getReturnType().toString())
                .build();
    }

    private static List<Method> methodsOf(Element element) {
        return element.getEnclosedElements().stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.METHOD)
                .map(enclosedElement -> {
                    ExecutableElement executableElement = (ExecutableElement) enclosedElement;
                    return Method.builder()
                            .name(executableElement.toString())
                            .parameters(parametersOf(executableElement))
                            .returnType(returnOf(executableElement))
                            .isStatic(executableElement.getModifiers().contains(Modifier.STATIC))
                            .build();
                })
                .toList();
    }

    private record AnnotatedElement(Element element, Set<? extends TypeElement> annotations) {

        Optional<? extends TypeElement> find(Class<? extends Annotation> annotation) {
            return annotations.stream()
                    .filter(a -> a.getQualifiedName().contentEquals(annotation.getName()))
                    .findFirst();
        }

    }

}
