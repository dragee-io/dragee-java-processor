package io.dragee.processor;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.model.Constructor;
import io.dragee.model.Field;
import io.dragee.model.Method;
import io.dragee.model.Parameter;
import io.dragee.model.Return;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

record DrageeElement(Element element, Set<? extends TypeElement> annotations) {

    public String name() {
        return element.toString();
    }

    public String kind() {
        List<String> kinds = annotations.stream()
                .flatMap(annotation -> Stream.ofNullable(annotation.getAnnotation(io.dragee.annotation.Dragee.class)))
                .map(io.dragee.annotation.Dragee::value)
                .sorted()
                .toList();

        if (kinds.size() != 1) {
            throw new DrageeCanNotBeOfMultipleKinds(name(), kinds);
        }

        return kinds.get(0);
    }

    public List<Constructor> constructors() {
        return element.getEnclosedElements().stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.CONSTRUCTOR)
                .map(enclosedElement -> Constructor.builder()
                        .parameters(parametersOf((ExecutableElement) enclosedElement))
                        .build())
                .toList();
    }

    public List<Field> fields() {
        return element.getEnclosedElements().stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .map(enclosedElement -> Field.builder()
                        .type(enclosedElement.asType().toString())
                        .name(enclosedElement.toString())
                        .build())
                .toList();
    }

    public List<Method> methods() {
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

}
