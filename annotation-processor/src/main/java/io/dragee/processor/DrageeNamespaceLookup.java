package io.dragee.processor;

import io.dragee.annotation.Dragee;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class DrageeNamespaceLookup {

    public Optional<DrageeNamespace> findCandidates(TypeElement potentialDrageeType) {
        List<DrageeNamespace> namespaces = lookupForNamespaces(potentialDrageeType);

        if (namespaces.isEmpty()) {
            return Optional.empty();
        }

        DrageeNamespace namespace = namespaces.get(0);

        if (namespaces.size() > 1) {
            DrageeNamespace.Part subNamespaceThatIsWrong = namespace.lastPart();
            List<DrageeNamespace> namespacesWithoutSubNamespace = namespaces.stream()
                    .map(DrageeNamespace::parent)
                    .toList();

            throw new WrongUsageOfNamespaces(subNamespaceThatIsWrong, namespacesWithoutSubNamespace);
        }

        return Optional.of(namespace);
    }

    private static List<DrageeNamespace> lookupForNamespaces(TypeElement annotationThatRepresentsDrageeType) {
        List<List<DrageeNamespace.Part>> exploredPaths = new ArrayList<>();

        continueUntilAllNamespacesAreFound(annotationThatRepresentsDrageeType, new ArrayList<>(), exploredPaths);

        return exploredPaths.stream()
                .filter(paths -> !paths.isEmpty())
                .peek(Collections::reverse)
                .map(DrageeNamespace::from)
                .toList();
    }

    private static void continueUntilAllNamespacesAreFound(TypeElement currentPart,
                                                           List<DrageeNamespace.Part> processedParts,
                                                           List<List<DrageeNamespace.Part>> exploredPaths) {
        List<TypeElement> potentialParts = currentPart.getAnnotationMirrors().stream()
                .map(annotationMirror -> (TypeElement) annotationMirror.getAnnotationType().asElement())
                .filter(annotation -> annotation.getAnnotation(Dragee.Namespace.class) != null)
                .toList();

        if (potentialParts.isEmpty()) {
            exploredPaths.add(processedParts);
        }

        potentialParts.forEach(nextPart -> {
            List<DrageeNamespace.Part> branch = new ArrayList<>(processedParts);
            branch.add(DrageeNamespace.Part.from(nextPart));
            continueUntilAllNamespacesAreFound(nextPart, branch, exploredPaths);
        });
    }

}
