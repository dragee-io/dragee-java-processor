package io.dragee.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

record DrageeAnnotation(TypeElement annotation) {

    public String kind() {
        return annotation.getAnnotation(io.dragee.annotation.Dragee.class).value();
    }

    public boolean isPresentOn(Element element) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(annotationMirror ->
                        annotationMirror.getAnnotationType()
                                .asElement().equals(annotation));
    }

}
