package io.draje.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Constructor(List<Parameter> parameters) {
}
