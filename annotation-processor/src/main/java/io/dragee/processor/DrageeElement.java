package io.dragee.processor;

import io.dragee.model.Dependency;
import lombok.Builder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

record DrageeElement(Element element, Kind kind) {

    public String name() {
        return element.toString();
    }

    public Optional<Dependency> findDependencyWith(DrageeElement otherElement) {
        Set<Dependency.Type> dependencyTypes = new HashSet<>();

        boolean inConstructor = constructors().stream().anyMatch(constructor -> constructor.contains(otherElement));
        if (inConstructor) {
            dependencyTypes.add(Dependency.Type.CONSTRUCTOR);
        }

        boolean asField = fields().stream().anyMatch(field -> field.isOrGeneric(otherElement));
        if (asField) {
            dependencyTypes.add(Dependency.Type.FIELD);
        }

        boolean asMethodParam = exposedMethods().stream().anyMatch(method -> method.use(otherElement));
        if (asMethodParam) {
            dependencyTypes.add(Dependency.Type.METHOD_PARAM);
        }

        boolean asMethodReturn = exposedMethods().stream().anyMatch(method -> method.returns(otherElement));
        if (asMethodReturn) {
            dependencyTypes.add(Dependency.Type.METHOD_RETURN);
        }

        if (dependencyTypes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Dependency.of(otherElement.name(), Set.copyOf(dependencyTypes)));
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

    public List<Method> exposedMethods() {
        return methods().stream()
                .filter(method -> !method.isPrivate())
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
                            .isPrivate(executableElement.getModifiers().contains(Modifier.PRIVATE))
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

    public record Kind(String value) {
        public static Kind of(String value) {
            return new Kind(value);
        }
    }

    private interface HasType {

        String type();

        default boolean isOrGeneric(DrageeElement element) {
            return type().contains(element.name());
        }

    }

    @Builder
    public record Constructor(List<Parameter> parameters) {

        boolean contains(DrageeElement element) {
            return parameters.stream()
                    .anyMatch(parameter -> parameter.isOrGeneric(element));
        }

    }

    @Builder
    public record Field(String name, String type) implements HasType {
    }

    @Builder
    public record Method(String name, boolean isPrivate, List<Parameter> parameters, Return returnType) {

        public boolean use(DrageeElement otherElement) {
            return parameters.stream().anyMatch(parameter -> parameter.isOrGeneric(otherElement));
        }

        public boolean returns(DrageeElement otherElement) {
            return returnType.isOrGeneric(otherElement);
        }
    }

    @Builder
    public record Parameter(String name, String type) implements HasType {
    }

    @Builder
    public record Return(String type) implements HasType {
    }

}
