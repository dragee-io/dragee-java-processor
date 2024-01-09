package io.dragee.processor;

import io.dragee.model.Dragee;

import java.util.Collection;
import java.util.function.Function;

class DrageeConverter implements Function<DrageeElements, Collection<Dragee>> {

    @Override
    public Collection<Dragee> apply(DrageeElements drageeElements) {
        return drageeElements.stream()
                .map(element -> toDragee(element, drageeElements))
                .toList();
    }

    private static Dragee toDragee(DrageeElement drageeElement, DrageeElements drageeElements) {
        return Dragee.builder()
                .fullName(drageeElement.name())
                .kindOf(drageeElement.kind().value())
                .dependsOn(drageeElements.dependenciesOf(drageeElement))
                .build();
    }

}
