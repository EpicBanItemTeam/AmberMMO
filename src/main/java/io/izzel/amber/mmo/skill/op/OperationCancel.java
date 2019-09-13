package io.izzel.amber.mmo.skill.op;

import io.izzel.amber.mmo.skill.CastingSkill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.entity.Entity;

@RequiredArgsConstructor
public class OperationCancel extends AbstractOperation<CastingSkill> {
    @Getter private final Entity source;
}
