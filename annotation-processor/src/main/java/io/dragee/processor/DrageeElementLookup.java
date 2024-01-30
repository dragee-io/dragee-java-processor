package io.dragee.processor;

import io.dragee.exception.DrageeCanNotBeOfMultipleKinds;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Comparator;
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

        final List<DrageeAnnotation> sortedAnnotations = drageeAnnotationsOnElement.stream()
                .sorted(Comparator.comparing(DrageeAnnotation::namespaceFullName))
                .toList();

        if (!annotationsInSameNamespace(sortedAnnotations)) {
            final List<String> kinds = drageeAnnotationsOnElement.stream()
                    .map(DrageeAnnotation::kind)
                    .sorted()
                    .toList();

            throw new DrageeCanNotBeOfMultipleKinds(element.toString(), kinds);
        }

        DrageeElement drageeElement = new DrageeElement(element, DrageeElement.Kind.of(getKindFromAnnotations(drageeAnnotationsOnElement)));
        return Optional.of(drageeElement);
    }

    private static boolean annotationsInSameNamespace(List<DrageeAnnotation> sortedAnnotations) {
        return sortedAnnotations
                .stream()
                .skip(1)
                .map(annotation -> {
                            final DrageeAnnotation previousAnnotation = sortedAnnotations.get(sortedAnnotations.indexOf(annotation) - 1);

                            final boolean currentNamespaceChildOfPreviousNamespaceForSameDragee =
                                    annotation.namespaceFullName().contains(previousAnnotation.namespaceFullName())
                                            && annotation.name().equals(previousAnnotation.name());

                            return currentNamespaceChildOfPreviousNamespaceForSameDragee;
                        }
                ).reduce((previous, current) -> previous && current)
                .orElse(sortedAnnotations.size() == 1);
    }


    private static String getKindFromAnnotations(Set<DrageeAnnotation> drageeAnnotationsOnElement){

        final String namespaceSeparator = "\\.";
        final String kindDelimiter = "/";

        final String fullNamespace = drageeAnnotationsOnElement
                .stream()
                .sorted(Comparator.comparing(DrageeAnnotation::namespaceFullName))
                .map(annotation -> {
                    var splitedNamespace = annotation.namespaceSimpleName().split(namespaceSeparator);
                    return splitedNamespace[splitedNamespace.length - 1];
                }).collect(Collectors.joining(kindDelimiter));

        final String annotationSimpleName = drageeAnnotationsOnElement.stream().findFirst().get().name();

        return String.join(kindDelimiter, fullNamespace, annotationSimpleName);
    }
}
