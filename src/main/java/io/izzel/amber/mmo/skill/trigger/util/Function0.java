package io.izzel.amber.mmo.skill.trigger.util;

import io.izzel.amber.mmo.skill.op.SkillOperation;

public interface Function0<O extends SkillOperation> extends OperateFunction<O> {

    O apply();

    @Override
    default O create(Object... args) {
        return apply();
    }

}
