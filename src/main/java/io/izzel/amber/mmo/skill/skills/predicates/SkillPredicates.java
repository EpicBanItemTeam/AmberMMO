package io.izzel.amber.mmo.skill.skills.predicates;

import io.izzel.amber.mmo.profession.ProfessionService;
import io.izzel.amber.mmo.profession.ProfessionSubject;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.data.SkillTree;
import org.spongepowered.api.event.entity.TargetEntityEvent;

import java.util.function.Predicate;

public final class SkillPredicates {

    public static <T extends TargetEntityEvent> Predicate<T> hasSkill(Class<? extends EntitySkill<?, ?>> cl) {
        return event -> ProfessionService.instance().getProfessions(event.getTargetEntity()).stream()
            .map(ProfessionSubject::getSkillTree).reduce(SkillTree::merge)
            .flatMap(it -> it.find(cl)).isPresent();
    }


}
