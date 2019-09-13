package io.izzel.amber.mmo.skill.op;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.entity.Entity;

public class SkillOperations {

    public static OperationTargetCast targetCast(Entity source, Entity... targets) {
        return new OperationTargetCast(source, ImmutableList.copyOf(targets));
    }
}
