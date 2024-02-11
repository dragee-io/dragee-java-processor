package io.dragee.processor;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;
import io.dragee.model.Kind;

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

    public DrageeElements findCandidates(DrageeAnnotations annotations,  RoundEnvironment roundEnv) {
        Set<DrageeElement> drageeElements = roundEnv.getRootElements().stream()
                .map(element -> testCandidate(annotations, element))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return DrageeElements.from(drageeElements);
    }

    private Optional<DrageeElement> testCandidate(DrageeAnnotations drageeAnnotations, Element element) {
        Set<DrageeAnnotation> drageeAnnotationsOnElement = drageeAnnotations.findPresentOrInheritedOn(element, types);

        if (drageeAnnotationsOnElement.isEmpty()) {
            return Optional.empty();
        }

        if (drageeAnnotationsOnElement.size() > 1) {
            List<String> kinds = drageeAnnotationsOnElement.stream()
                    .map(DrageeAnnotation::kind)
                    .sorted()
                    .toList();

            throw new DrageeCanNotBeOfMultipleKinds(element.toString(), kinds);
        }

        DrageeAnnotation drageeAnnotation = drageeAnnotationsOnElement.iterator().next();
        DrageeElement drageeElement = new DrageeElement(element, Kind.of(drageeAnnotation.kind()));
        return Optional.of(drageeElement);
    }

}
