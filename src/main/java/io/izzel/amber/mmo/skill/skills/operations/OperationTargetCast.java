package io.izzel.amber.mmo.skill.skills.operations;

import io.izzel.amber.mmo.skill.CastingSkill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.entity.Entity;

import java.util.List;

@RequiredArgsConstructor
public class OperationTargetCast extends AbstractOperation<CastingSkill<?>> {

    @Getter private final Entity source;
    @Getter private final List<Entity> targets;

}
