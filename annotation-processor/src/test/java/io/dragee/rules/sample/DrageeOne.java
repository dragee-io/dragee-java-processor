package io.dragee.rules.sample;

import io.dragee.testing.TestObject;

import java.util.List;
import java.util.Map;

@TestObject
public class DrageeOne {

    DrageeTwo drageeTwo;

    Map<String, List<List<DrageeFive>>> drageeInGenerics;

    DrageeOne(DrageeTwo inConstructor) {}

    DrageeFour handle(DrageeTwo drageeTwo, DrageeThree drageeThree) {
        return new DrageeFour();
    }

}
