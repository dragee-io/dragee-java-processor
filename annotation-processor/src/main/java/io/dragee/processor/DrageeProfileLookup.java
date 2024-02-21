package io.dragee.processor;

import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeProfileLookup {

    private final DrageeNamespaceLookup namespaceLookup;

    DrageeProfileLookup(DrageeNamespaceLookup namespaceLookup) {
        this.namespaceLookup = namespaceLookup;
    }

    public DrageeProfiles findCandidates(Set<? extends TypeElement> annotations) {
        Set<DrageeProfile> candidates = annotations.stream()
                .map(this::test)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return DrageeProfiles.from(candidates);
    }

    private Optional<DrageeProfile> test(TypeElement annotation) {
        return namespaceLookup.findCandidates(annotation)
                .map(namespace -> new DrageeProfile(namespace, DrageeType.from(annotation)));
    }


}
