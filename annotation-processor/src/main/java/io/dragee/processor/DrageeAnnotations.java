package io.dragee.processor;

import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.stream.Stream;

class DrageeAnnotations {

    private final Collection<DrageeAnnotation> annotations;

    private DrageeAnnotations(Collection<DrageeAnnotation> annotations) {
        this.annotations = annotations;
    }

    public TypeElement[] asTypeElements() {
        return annotations.stream()
                .map(DrageeAnnotation::element)
                .toArray(TypeElement[]::new);
    }

    public Stream<DrageeAnnotation> stream() {
        return annotations.stream();
    }

    public static DrageeAnnotations from(Collection<DrageeAnnotation> annotations) {
        return new DrageeAnnotations(annotations);
    }

}
