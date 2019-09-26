package io.izzel.amber.mmo.skill.trigger.dispatcher;

public interface Dispatcher4<A, B, C, D> extends OperateDispatcher {

    void apply(A p1, B p2, C p3, D p4);

    @SuppressWarnings("unchecked")
    @Override
    default void dispatch(Object... args) {
        apply((A) args[0], (B) args[1], (C) args[2], (D) args[3]);
    }
}
