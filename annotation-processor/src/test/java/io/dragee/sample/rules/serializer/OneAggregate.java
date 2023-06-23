package io.dragee.sample.rules.serializer;

import io.dragee.annotation.Aggregate;

@Aggregate
public class OneAggregate {

    OneValue oneValue;

    public OneAggregate(OneValue oneValue) {
        this.oneValue = oneValue;
    }

    public OneEvent handle(OneCommand command) {
        return null;
    }

}
