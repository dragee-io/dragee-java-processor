package io.dragee.model;

import lombok.Builder;

@Builder
public record Field(String name, String type) {
}
