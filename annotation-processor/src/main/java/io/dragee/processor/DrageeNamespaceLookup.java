package io.dragee.processor;

import io.dragee.annotation.Dragee;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class DrageeNamespaceLookup {

    public Optional<DrageeNamespace> findFrom(Element element) {
        List<Element> namespaceParts = lookupForNamespaceParts(element, new ArrayList<>(), new ArrayList<>());

        if (namespaceParts.isEmpty()) {
            return Optional.empty();
        }

        DrageeNamespace namespace = DrageeNamespace.from(namespaceParts);
        return Optional.of(namespace);
    }

    private static List<Element> lookupForNamespaceParts(Element element,
                                                         List<Element> alreadyLookedUp,
                                                         List<Element> namespaceParts) {
        Dragee.Namespace annotation = element.getAnnotation(Dragee.Namespace.class);

        if (annotation != null) {
            namespaceParts.add(element);
        }

        alreadyLookedUp.add(element);

        element.getAnnotationMirrors().stream()
                .map(annotationMirror -> annotationMirror.getAnnotationType().asElement())
                .filter(el -> !alreadyLookedUp.contains(el))
                .peek(alreadyLookedUp::add)
                .forEachOrdered(el -> lookupForNamespaceParts(el, alreadyLookedUp, namespaceParts));

        Collections.reverse(namespaceParts);

        return List.copyOf(namespaceParts);
    }
}
