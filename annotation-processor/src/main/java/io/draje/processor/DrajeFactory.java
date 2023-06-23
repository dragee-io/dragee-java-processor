package io.draje.processor;

import io.draje.model.Constructor;
import io.draje.model.Draje;
import io.draje.model.Field;
import io.draje.model.Method;
import io.draje.model.Parameter;
import io.draje.model.Return;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DrajeFactory {

    public List<Draje> createDrajes(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return annotations.stream()
                .map(annotation -> createDrajes(annotation, roundEnv))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Draje> createDrajes(TypeElement annotation, RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(annotation).stream()
                .map(element -> Draje.builder()
                        .name(element.toString())
                        .constructors(constructorsOf(element))
                        .fields(fieldsOf(element))
                        .methods(methodsOf(element))
                        .build())
                .toList();
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
                            .name(executableElement.asType().toString())
                            .parameters(parametersOf(executableElement))
                            .returnType(returnOf(executableElement))
                            .isStatic(executableElement.getModifiers().contains(Modifier.STATIC))
                            .build();
                })
                .toList();
    }

}
