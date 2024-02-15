package io.dragee.processor;

import io.dragee.util.SnakeCase;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.stream.Collectors;

class DrageeNamespace {

    private final List<Part> parts;

    private DrageeNamespace(List<Part> parts) {
        this.parts = List.copyOf(parts);
    }

    @Override
    public String toString() {
        return parts.stream()
                .map(Part::toString)
                .collect(Collectors.joining(DrageeProfile.SEPARATOR));
    }

    public static DrageeNamespace from(List<Part> parts) {
        return new DrageeNamespace(parts);
    }

    public Part lastPart() {
        return parts.get(parts.size() - 1);
    }

    public DrageeNamespace parent() {
        List<Part> withoutLastPart = parts.subList(0, parts.size() - 1);
        return DrageeNamespace.from(withoutLastPart);
    }

    public static final class Part {

        private final TypeElement annotation;

        Part(TypeElement annotation) {
            this.annotation = annotation;
        }

        @Override
        public String toString() {
            String simpleName = annotation.getSimpleName().toString();
            return SnakeCase.toSnakeCase(simpleName);
        }

        public static Part from(TypeElement annotation) {
            return new Part(annotation);
        }

    }

}
