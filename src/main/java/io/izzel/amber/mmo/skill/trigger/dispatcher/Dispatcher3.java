package io.izzel.amber.mmo.skill.trigger.dispatcher;

public interface Dispatcher3<A, B, C> extends OperateDispatcher {

    void apply(A p1, B p2, C p3);

    @SuppressWarnings("unchecked")
    @Override
    default void dispatch(Object... args) {
        apply((A) args[0], (B) args[1], (C) args[2]);
    }
}
