package io.dragee.model;

import java.util.Set;

public record Dragee(
        String kindOf,
        String fullName,
        Set<Dependency> dependsOn) {

    public String shortName() {
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String kindOf;
        private String fullName;
        private Set<Dependency> dependencies;

        public Builder kindOf(String kindOf) {
            this.kindOf = kindOf;
            return this;
        }

        public Builder fullName(String name) {
            this.fullName = name;
            return this;
        }

        public Builder dependsOn(Set<Dependency> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public Dragee build() {
            return new Dragee(kindOf, fullName, dependencies);
        }

    }
}
