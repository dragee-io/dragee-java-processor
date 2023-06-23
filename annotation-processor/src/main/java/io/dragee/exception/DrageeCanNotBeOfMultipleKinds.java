package io.dragee.exception;

import java.util.List;

public class DrageeCanNotBeOfMultipleKinds extends DrageeException {

    public DrageeCanNotBeOfMultipleKinds(String name, List<String> kinds) {
        super(String.format("Dragee can not be of multiple kinds: '%s', [%s]",
                name,
                String.join(",",kinds)));
    }

}
