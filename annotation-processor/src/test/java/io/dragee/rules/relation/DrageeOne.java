package io.dragee.rules.relation;

import io.dragee.testing.TestObject;

import java.util.List;
import java.util.Map;

@TestObject
public class DrageeOne {

    DrageeTwo drageeTwo;

    Map<String, List<List<DrageeFive>>> drageeInGenerics;

    DrageeOne(DrageeTwo inConstructor, List<DrageeThree> listOfDrageeThree) {}

    DrageeFour handle(DrageeTwo drageeTwo, Map<DrageeThree, String> mapWithDrageeThree) {
        return new DrageeFour();
    }

    List<DrageeFive> handle(Integer someValue) {
        return List.of();
    }

    private DrageeSix handle(String someValue) {
        return new DrageeSix();
    }

    private void handle(DrageeSeven drageeSeven) {
    }

}
