package io.dragee.exception;

import java.util.Collection;

public class WrongUsageOfNamespaces extends DrageeException {

    public WrongUsageOfNamespaces(String wrongNamespace) {
        super(String.format("A sub namespace can not depend on more than one other namespace: [%s]",
                wrongNamespace));
    }

}
