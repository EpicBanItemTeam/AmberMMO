package io.izzel.amber.mmo.skill.trigger.dispatcher;

public interface Dispatcher2<A, B> extends OperateDispatcher {

    void apply(A p1, B p2);

    @SuppressWarnings("unchecked")
    @Override
    default void dispatch(Object... args) {
        apply((A) args[0], (B) args[1]);
    }
}
