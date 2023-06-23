package io.draje.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Draje(
        String name,
        List<Constructor> constructors,
        List<Field> fields,
        List<Method> methods) {
}
