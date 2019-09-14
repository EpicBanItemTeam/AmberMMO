package io.izzel.amber.mmo.skill.trigger.util;

import io.izzel.amber.mmo.skill.op.SkillOperation;

public interface OperateFunction<O extends SkillOperation> {
    O create(Object... args);
}
