package io.izzel.amber.mmo.skill.trigger.dispatcher;

public interface Dispatcher1<A> extends OperateDispatcher {

    void apply(A p1);

    @SuppressWarnings("unchecked")
    @Override
    default void dispatch(Object... args) {
        apply(((A) args[0]));
    }

}
