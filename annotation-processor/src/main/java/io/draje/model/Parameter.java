package io.draje.model;

import lombok.Builder;

@Builder
public record Parameter(String name, String type) {
}
