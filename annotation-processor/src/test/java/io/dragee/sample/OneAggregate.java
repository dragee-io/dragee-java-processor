package io.dragee.sample;

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
