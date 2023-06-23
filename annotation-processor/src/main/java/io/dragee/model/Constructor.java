package io.dragee.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Constructor(List<Parameter> parameters) {
}
