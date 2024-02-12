package io.dragee.exception;

import java.util.Collection;

public class WrongUsageOfProfiles extends DrageeException {

    public WrongUsageOfProfiles(String name, Collection<String> profiles) {
        super(String.format("Dragee can not have more than one profile: '%s', [%s]",
                name,
                String.join(", ", profiles)));
    }

}
