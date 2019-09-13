package io.izzel.amber.mmo.skill;

import io.izzel.amber.mmo.skill.data.EntitySkill;

/**
 * 设计为释放的技能实体（有使用进度等）
 */
public abstract class CastingSkill<E extends EntitySkill> {

    public abstract E getOwning();

    public abstract SkillSubject getSubject();

    protected abstract void perform(SkillOperation operation, SkillSubject.CastOperator operator) throws UnsupportedOperationException;

}
