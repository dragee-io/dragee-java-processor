package io.dragee.rules.dragee_extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@DrageeGrandParent
@Target({ElementType.TYPE})
public @interface DrageeParent {

    @DrageeParent
    @Target({ElementType.TYPE})
    @interface DrageeChild {
    }

}
