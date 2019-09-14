package io.izzel.amber.mmo.skill.trigger.util;

import io.izzel.amber.mmo.skill.SkillOperation;

public interface Function3<A, B, C, O extends SkillOperation> extends OperateFunction<O> {

    O apply(A p1, B p2, C p3);

    @SuppressWarnings("unchecked")
    @Override
    default O create(Object... args) {
        return apply((A) args[0], (B) args[1], (C) args[2]);
    }
}
