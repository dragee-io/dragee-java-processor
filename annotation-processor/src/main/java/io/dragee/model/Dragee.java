package io.dragee.model;

import lombok.Builder;

import java.util.Set;

@Builder
public record Dragee(
        String kindOf,
        String name,
        Set<Dependency> dependsOn) {

    public String namespace() {
        return name.substring(0, name.lastIndexOf("."));
    }

    public String shortName() {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
