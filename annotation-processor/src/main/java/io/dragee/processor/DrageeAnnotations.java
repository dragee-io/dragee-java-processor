package io.dragee.processor;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DrageeAnnotations {

    private final Collection<DrageeAnnotation> annotations;

    private DrageeAnnotations(Collection<DrageeAnnotation> annotations) {
        this.annotations = annotations;
    }

    public Set<DrageeAnnotation> findPresentOrInheritedOn(Element element, Types types) {
        return annotations.stream()
                .filter(drageeAnnotation -> drageeAnnotation.isPresentOrInheritedOn(element, types))
                .collect(Collectors.toSet());
    }

    public Stream<DrageeAnnotation> stream() {
        return annotations.stream();
    }

    public static DrageeAnnotations from(Collection<DrageeAnnotation> annotations) {
        return new DrageeAnnotations(annotations);
    }

}
