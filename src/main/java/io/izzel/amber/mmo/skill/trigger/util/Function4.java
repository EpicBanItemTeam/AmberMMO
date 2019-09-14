package io.izzel.amber.mmo.skill.trigger.util;

import io.izzel.amber.mmo.skill.SkillOperation;

public interface Function4<A, B, C, D, O extends SkillOperation> extends OperateFunction<O> {

    O apply(A p1, B p2, C p3, D p4);

    @SuppressWarnings("unchecked")
    @Override
    default O create(Object... args) {
        return apply((A) args[0], (B) args[1], (C) args[2], (D) args[3]);
    }
}
