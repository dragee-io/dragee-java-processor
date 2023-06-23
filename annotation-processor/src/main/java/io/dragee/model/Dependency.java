package io.dragee.model;

import java.util.Set;

public record Dependency(String otherDragee, Set<Type> types) {

    public static Dependency of(String otherDragee, Set<Type> types) {
        return new Dependency(otherDragee, types);
    }

    public enum Type {
        CONSTRUCTOR, FIELD, METHOD_PARAM, METHOD_RETURN;
    }

}
