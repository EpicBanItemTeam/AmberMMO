package io.izzel.amber.mmo.skill.skills.operations;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.entity.Entity;

public class SkillOperations {

    public static OperationTargetCast targetCast(Entity source, Entity... targets) {
        return new OperationTargetCast(source, ImmutableList.copyOf(targets));
    }

    public static OperationCancel cancel(Entity source) {
        return new OperationCancel(source);
    }
}
