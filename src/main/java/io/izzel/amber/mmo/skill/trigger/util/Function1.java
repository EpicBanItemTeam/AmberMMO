package io.izzel.amber.mmo.skill.trigger.util;

import io.izzel.amber.mmo.skill.SkillOperation;

public interface Function1<A, O extends SkillOperation> extends OperateFunction<O> {

    O apply(A p1);

    @SuppressWarnings("unchecked")
    @Override
    default O create(Object... args) {
        return apply(((A) args[0]));
    }

}
