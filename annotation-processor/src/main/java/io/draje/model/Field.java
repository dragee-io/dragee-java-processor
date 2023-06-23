package io.draje.model;

import lombok.Builder;

@Builder
public record Field(String name, String type) {
}
