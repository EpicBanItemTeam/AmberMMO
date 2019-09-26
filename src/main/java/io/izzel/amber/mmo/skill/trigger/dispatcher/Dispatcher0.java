package io.izzel.amber.mmo.skill.trigger.dispatcher;

import io.izzel.amber.mmo.skill.SkillOperation;

public interface Dispatcher0<O extends SkillOperation> extends OperateDispatcher {

    void apply();

    @Override
    default void dispatch(Object... args) {
        apply();
    }

}
