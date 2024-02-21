package io.dragee.processor;

import io.dragee.exception.DrageeException;

import java.util.Collection;
import java.util.stream.Collectors;

public class WrongUsageOfProfiles extends DrageeException {

    WrongUsageOfProfiles(String name, Collection<DrageeProfile> profiles) {
        super(String.format("Dragee can not have more than one profile: '%s', [%s]",
                name,
                toString(profiles)));
    }

    private static String toString(Collection<DrageeProfile> profiles) {
        return profiles.stream()
                .map(DrageeProfile::toString)
                .sorted(String::compareTo)
                .collect(Collectors.joining(", "));
    }

}
