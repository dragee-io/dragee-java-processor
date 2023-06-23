package io.dragee.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Dragee(
        String kindOf,
        String name,
        List<Constructor> constructors,
        List<Field> fields,
        List<Method> methods) {

    public String namespace() {
        return name.substring(0, name.lastIndexOf("."));
    }

    public String shortName() {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
