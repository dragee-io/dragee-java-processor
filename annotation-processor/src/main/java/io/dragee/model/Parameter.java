package io.dragee.model;

import lombok.Builder;

@Builder
public record Parameter(String name, String type) {
}
