package io.dragee.processor;

import io.dragee.exception.DrageeException;

import java.util.Collection;
import java.util.stream.Collectors;

public class WrongUsageOfNamespaces extends DrageeException {

    WrongUsageOfNamespaces(DrageeNamespace.Part subNamespace, Collection<DrageeNamespace> namespaces) {
        super(String.format("A sub namespace can not depend on more than one other namespace: '%s', [%s]",
                subNamespace, toString(namespaces)));
    }

    private static String toString(Collection<DrageeNamespace> namespaces) {
        return namespaces.stream()
                .map(DrageeNamespace::toString)
                .sorted(String::compareTo)
                .map(WrongUsageOfNamespaces::quote)
                .collect(Collectors.joining(", "));
    }

    private static String quote(String stringToQuote) {
        return "'%s'".formatted(stringToQuote);
    }

}
