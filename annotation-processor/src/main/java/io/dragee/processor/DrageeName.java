package io.dragee.processor;

import io.dragee.util.SimpleName;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static io.dragee.util.SnakeCase.toSnakeCase;

public class DrageeName {

    private final TypeElement element;

    DrageeName(TypeElement element) {
        this.element = element;
    }

    public boolean isBasedOn(Element element) {
        return this.element.equals(element);
    }

    @Override
    public String toString() {
        String annotationName = element.getQualifiedName().toString();
        String simpleName = SimpleName.toSimpleName(annotationName);
        return toSnakeCase(simpleName);
    }

    public static DrageeName from(TypeElement element) {
        return new DrageeName(element);
    }
}
