package io.izzel.amber.mmo.skill.skills.operations;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import org.spongepowered.api.entity.Entity;

public interface OperationSourced<C extends CastingSkill> extends SkillOperation<C> {

    Entity getSource();

}
