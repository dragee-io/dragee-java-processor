package io.dragee.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Method(String name, List<Parameter> parameters, Return returnType, boolean isStatic) {
}
