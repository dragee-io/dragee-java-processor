package io.dragee.processor;

import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DrageeProfiles {

    private final Collection<DrageeProfile> profiles;

    private DrageeProfiles(Collection<DrageeProfile> profiles) {
        this.profiles = profiles;
    }

    public Set<DrageeProfile> findPresentOrInheritedOn(Element element, Types types) {
        return profiles.stream()
                .filter(drageeProfile -> drageeProfile.isPresentOrInheritedOn(element, types))
                .collect(Collectors.toSet());
    }

    public Stream<DrageeProfile> stream() {
        return profiles.stream();
    }

    public static DrageeProfiles from(Collection<DrageeProfile> profiles) {
        return new DrageeProfiles(profiles);
    }

}
