package io.dragee.processor;

import java.util.Collection;
import java.util.stream.Stream;

class DrageeElements {

    private final Collection<DrageeElement> elements;

    private DrageeElements(Collection<DrageeElement> elements) {
        this.elements = elements;
    }

    public Stream<DrageeElement> stream() {
        return elements.stream();
    }

    public static DrageeElements from(Collection<DrageeElement> elements) {
        return new DrageeElements(elements);
    }

}
