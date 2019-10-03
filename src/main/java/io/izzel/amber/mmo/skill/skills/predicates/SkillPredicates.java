package io.izzel.amber.mmo.skill.skills.predicates;

import io.izzel.amber.mmo.profession.ProfessionService;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import org.spongepowered.api.event.entity.TargetEntityEvent;

import java.util.function.Predicate;

public final class SkillPredicates {

    public static <T extends TargetEntityEvent> Predicate<T> hasSkill(Class<? extends EntitySkill<?, ?>> cl) {
        return event -> ProfessionService.instance().getOrCreate(event.getTargetEntity()).getMerged().find(cl).isPresent();
    }


}
