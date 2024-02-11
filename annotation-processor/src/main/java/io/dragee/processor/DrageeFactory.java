package io.dragee.processor;

import io.dragee.model.Dragee;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class DrageeFactory {

    private final DrageeAnnotationLookup drageeAnnotationLookup;
    private final DrageeElementLookup drageeElementLookup;
    private final DrageeConverter drageeConverter;

    public DrageeFactory(Types types) {
        DrageeNamespaceLookup drageeNamespaceLookup = new DrageeNamespaceLookup();
        this.drageeAnnotationLookup = new DrageeAnnotationLookup(drageeNamespaceLookup);
        this.drageeElementLookup = new DrageeElementLookup(types);
        this.drageeConverter = new DrageeConverter();
    }

    public Collection<Dragee> createDragees(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        DrageeElements drageeElements = findDrageeElements(annotations, roundEnv);
        return drageeConverter.apply(drageeElements);
    }

    private DrageeElements findDrageeElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        DrageeAnnotations drageeAnnotations = drageeAnnotationLookup.findCandidates(annotations);
        return drageeElementLookup.findCandidates(drageeAnnotations, roundEnv);
    }



}
