package io.dragee.processor;

import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeAnnotationLookup {

    private final DrageeNamespaceLookup namespaceLookup;

    DrageeAnnotationLookup(DrageeNamespaceLookup namespaceLookup) {
        this.namespaceLookup = namespaceLookup;
    }

    public DrageeAnnotations findCandidates(Set<? extends TypeElement> annotations) {
        Set<DrageeAnnotation> candidates = annotations.stream()
                .map(this::test)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return DrageeAnnotations.from(candidates);
    }

    private Optional<DrageeAnnotation> test(TypeElement annotationElement) {
        DrageeName name = DrageeName.from(annotationElement);

        return namespaceLookup.findFrom(annotationElement)
                .map(namespace -> new DrageeAnnotation(namespace, name));
    }


}
