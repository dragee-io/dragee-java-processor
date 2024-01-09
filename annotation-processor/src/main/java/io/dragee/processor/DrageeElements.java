package io.dragee.processor;

import io.dragee.model.Dependency;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DrageeElements {

    private final Collection<DrageeElement> elements;

    private DrageeElements(Collection<DrageeElement> elements) {
        this.elements = elements;
    }

    public Set<Dependency> dependenciesOf(DrageeElement element) {
        return elements.stream()
                .map(element::findDependencyWith)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    public Stream<DrageeElement> stream() {
        return elements.stream();
    }

    public static DrageeElements from(Collection<DrageeElement> elements) {
        return new DrageeElements(elements);
    }

}
