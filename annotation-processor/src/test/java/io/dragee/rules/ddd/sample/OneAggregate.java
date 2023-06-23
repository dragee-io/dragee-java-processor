package io.dragee.rules.ddd.sample;

import io.dragee.annotation.ddd.Aggregate;

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
