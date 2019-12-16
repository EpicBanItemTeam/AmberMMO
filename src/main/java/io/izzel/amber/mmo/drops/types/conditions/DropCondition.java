package io.izzel.amber.mmo.drops.types.conditions;

import java.util.function.Predicate;

public interface DropCondition extends Predicate<Void> {

    boolean test();

    @Override
    default boolean test(Void v) {
        return test();
    }

    static DropCondition constant(boolean b) {
        return () -> b;
    }

}
