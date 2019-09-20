package io.izzel.amber.mmo.skill.skills.operations;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import org.spongepowered.api.entity.Entity;

import java.util.Collection;

public interface OperationTargeted<C extends CastingSkill> extends SkillOperation<C> {

    Entity getSource();

    Collection<Entity> getTargets();

}
