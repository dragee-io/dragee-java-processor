package io.dragee.processor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeAnnotationLookup {

    public DrageeAnnotations findCandidates(Set<? extends TypeElement> annotations) {
        Set<DrageeAnnotation> candidates =
                annotations
                        .stream()
                        .map(DrageeAnnotation::test)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());

        return DrageeAnnotations.from(candidates);
    }


}