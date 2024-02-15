package io.dragee.processor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeElementLookup {

    private final Types types;

    DrageeElementLookup(Types types) {
        this.types = types;
    }

    public DrageeElements findCandidates(DrageeProfiles annotations, RoundEnvironment roundEnv) {
        Set<DrageeElement> drageeElements = roundEnv.getRootElements().stream()
                .map(element -> testCandidate(annotations, element))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return DrageeElements.from(drageeElements);
    }

    private Optional<DrageeElement> testCandidate(DrageeProfiles drageeProfiles, Element element) {
        Set<DrageeProfile> drageeProfilesOnElement = drageeProfiles.findPresentOrInheritedOn(element, types);

        if (drageeProfilesOnElement.isEmpty()) {
            return Optional.empty();
        }

        if (drageeProfilesOnElement.size() > 1) {
            throw new WrongUsageOfProfiles(element.toString(), drageeProfilesOnElement);
        }

        DrageeProfile drageeProfile = drageeProfilesOnElement.iterator().next();
        DrageeElement drageeElement = new DrageeElement(element, drageeProfile);
        return Optional.of(drageeElement);
    }

}
