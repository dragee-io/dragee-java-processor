package io.dragee.processor;

import io.dragee.model.Dependency;
import io.dragee.model.Kind;

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
                .map(enclosedElement -> {
                    List<Parameter> parameters = parametersOf((ExecutableElement) enclosedElement);
                    return Constructor.withParameters(parameters);
                })
                .toList();
    }

    public List<Field> fields() {
        return element.getEnclosedElements().stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .map(enclosedElement -> {
                    String type = enclosedElement.asType().toString();
                    String name = enclosedElement.toString();

                    return Field.of(type, name);
                })
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
                .map(parameter -> {
                    String type = parameter.asType().toString();
                    String name = parameter.toString();

                    return Parameter.of(type, name);
                })
                .toList();
    }

    private static Return returnOf(ExecutableElement element) {
        return Return.ofType(element.getReturnType().toString());
    }

    private interface HasType {

        String type();

        default boolean isOrGeneric(DrageeElement element) {
            return type().contains(element.name());
        }

    }

    record Constructor(List<Parameter> parameters) {

        public static Constructor withParameters(List<Parameter> parameters) {
            return new Constructor(parameters);
        }

        boolean contains(DrageeElement element) {
            return parameters.stream()
                    .anyMatch(parameter -> parameter.isOrGeneric(element));
        }

    }

    record Field(String type, String name) implements HasType {
        public static Field of(String type, String name) {
            return new Field(type, name);
        }
    }

    record Parameter(String type, String name) implements HasType {

        public static Parameter of(String type, String name) {
            return new Parameter(type, name);
        }
    }
    record Return(String type) implements HasType {

        public static Return ofType(String type) {
            return new Return(type);
        }
    }
    record Method(String name, boolean isPrivate, List<Parameter> parameters, Return returnType) {

        public boolean use(DrageeElement otherElement) {
            return parameters.stream().anyMatch(parameter -> parameter.isOrGeneric(otherElement));
        }

        public boolean returns(DrageeElement otherElement) {
            return returnType.isOrGeneric(otherElement);
        }

        public static Builder builder() {
            return new Builder();
        }

        static final class Builder {

            private String name;
            private boolean isPrivate;
            private List<Parameter> parameters;
            private Return returnType;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder isPrivate(boolean isPrivate) {
                this.isPrivate = isPrivate;
                return this;
            }

            public Builder parameters(List<Parameter> parameters) {
                this.parameters = parameters;
                return this;
            }

            public Builder returnType(Return returnType) {
                this.returnType = returnType;
                return this;
            }

            public Method build() {
                return new Method(name, isPrivate, parameters, returnType);
            }

        }

    }

}
