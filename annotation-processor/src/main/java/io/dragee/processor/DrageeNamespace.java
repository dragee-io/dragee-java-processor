package io.dragee.processor;

import io.dragee.util.SnakeCase;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import java.util.List;
import java.util.stream.Collectors;

public class DrageeNamespace {

    private final List<Element> parts;

    private DrageeNamespace(List<Element> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        String joinedParts = parts.stream()
                .map(Element::getSimpleName)
                .map(Name::toString)
                .collect(Collectors.joining(DrageeProfile.SEPARATOR));

        return SnakeCase.toSnakeCase(joinedParts);
    }

    public static DrageeNamespace from(List<Element> parts) {
        return new DrageeNamespace(parts);
    }

}
