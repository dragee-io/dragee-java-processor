package io.dragee.processor;

import io.dragee.model.Dragee;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class DrageeConverter implements Function<DrageeElements, Collection<Dragee>> {

    @Override
    public Collection<Dragee> apply(DrageeElements drageeElements) {
        return drageeElements.stream()
                .map(element -> toDragee(element, drageeElements))
                .toList();
    }

    private static Dragee toDragee(DrageeElement drageeElement, DrageeElements drageeElements) {
        return Dragee.builder()
                .name(drageeElement.name())
                .kindOf(drageeElement.kind().value())
                .dependsOn(drageeElements.stream()
                        .map(drageeElement::findDependencyWith)
                        .flatMap(Optional::stream)
                        .collect(Collectors.toSet()))
                .build();
    }

}
