package io.dragee.processor;

import io.dragee.annotation.Inheritable;
import io.dragee.util.SimpleName;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static io.dragee.util.SnakeCase.toSnakeCase;

public class DrageeType {

    private final TypeElement element;

    DrageeType(TypeElement element) {
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

    public static DrageeType from(TypeElement element) {
        return new DrageeType(element);
    }

    public boolean isInheritable() {
        return this.element.getAnnotation(Inheritable.class) != null;
    }
}
