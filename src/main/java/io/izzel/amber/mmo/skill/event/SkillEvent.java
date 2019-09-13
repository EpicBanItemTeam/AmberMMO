package io.izzel.amber.mmo.skill.event;

import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.helper.ReflectiveEntitySkill;
import io.izzel.amber.mmo.skill.helper.ReflectiveEntitySkillBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.entity.TargetEntityEvent;

import java.util.List;

public interface SkillEvent extends Event {

    interface Registry extends SkillEvent {

        <T extends EntitySkill<?, ?>> void registerSkill(String typeId, Class<T> cl, AbstractDataBuilder<T> builder);

        default <E extends ReflectiveEntitySkill> AbstractDataBuilder<E> createReflectiveBuilder(Class<E> cl) {
            return new ReflectiveEntitySkillBuilder<>(cl);
        }

    }

    interface Cast extends SkillEvent, TargetEntityEvent, Cancellable {

        List<EntitySkill> getCastSkills();

        void setCastSkills(List<EntitySkill> skills);

    }

    interface Obtain extends SkillEvent, TargetEntityEvent, Cancellable {}

    interface Lose extends SkillEvent, TargetEntityEvent, Cancellable {}

}
