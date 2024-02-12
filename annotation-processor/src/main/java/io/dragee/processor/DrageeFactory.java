package io.dragee.processor;

import io.dragee.model.Dragee;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.Set;

class DrageeFactory {

    private final DrageeProfileLookup drageeProfileLookup;
    private final DrageeElementLookup drageeElementLookup;
    private final DrageeConverter drageeConverter;

    public DrageeFactory(Types types) {
        DrageeNamespaceLookup drageeNamespaceLookup = new DrageeNamespaceLookup();
        this.drageeProfileLookup = new DrageeProfileLookup(drageeNamespaceLookup);
        this.drageeElementLookup = new DrageeElementLookup(types);
        this.drageeConverter = new DrageeConverter();
    }

    public Collection<Dragee> createDragees(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        DrageeElements drageeElements = findDrageeElements(annotations, roundEnv);
        return drageeConverter.apply(drageeElements);
    }

    private DrageeElements findDrageeElements(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        DrageeProfiles drageeProfiles = drageeProfileLookup.findCandidates(annotations);
        return drageeElementLookup.findCandidates(drageeProfiles, roundEnv);
    }



}
