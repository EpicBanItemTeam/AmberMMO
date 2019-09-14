package io.izzel.amber.mmo.skill.event;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.op.SkillOperation;
import lombok.Data;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;

@Data
class SkillOperateEventImpl<C extends CastingSkill, O extends SkillOperation> implements SkillEvent.Operate<C, O> {

    SkillOperateEventImpl(Cause cause, Entity targetEntity, C castingSkill, O operation) {
        this.cause = cause;
        this.targetEntity = targetEntity;
        this.castingSkill = castingSkill;
        this.operation = operation;
    }

    private final Cause cause;
    private final Entity targetEntity;
    private C castingSkill;
    private O operation;
    private boolean cancelled = false;
}
