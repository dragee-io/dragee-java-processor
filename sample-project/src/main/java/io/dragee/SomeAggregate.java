package io.dragee;

import io.dragee.annotation.Aggregate;

import java.util.List;
import java.util.Map;

@Aggregate
public class SomeAggregate {

    Map<String, List<List<SomeValueObject>>> oneValue;

}
