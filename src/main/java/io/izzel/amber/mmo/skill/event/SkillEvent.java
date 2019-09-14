package io.izzel.amber.mmo.skill.event;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.helper.ReflectiveEntitySkill;
import io.izzel.amber.mmo.skill.helper.ReflectiveEntitySkillBuilder;
import io.izzel.amber.mmo.skill.op.SkillOperation;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.TargetEntityEvent;

import java.util.Map;

public interface SkillEvent extends Event {

    interface Registry extends SkillEvent {

        <T extends EntitySkill<?, ?>> void registerSkill(String typeId, Class<T> cl, AbstractDataBuilder<T> builder);

        default <E extends ReflectiveEntitySkill> AbstractDataBuilder<E> createReflectiveBuilder(Class<E> cl) {
            return new ReflectiveEntitySkillBuilder<>(cl);
        }

    }

    interface Operate<C extends CastingSkill, O extends SkillOperation> extends SkillEvent, TargetEntityEvent, Cancellable {

        C getCastingSkill();

        O getOperation();

        void setCastingSkill(C castingSkill);

        void setOperation(O operation);

    }

    interface Obtain extends SkillEvent, TargetEntityEvent, Cancellable {}

    interface Lose extends SkillEvent, TargetEntityEvent, Cancellable {}

    static Registry createRegistry(Cause cause, Map<String, Class<?>> map) {
        return new SkillRegistryEventImpl(cause, map);
    }

    static <C extends CastingSkill, O extends SkillOperation> Operate<C, O> createOperate(Cause cause, Entity entity, C c, O o) {
        return new SkillOperateEventImpl<>(cause, entity, c, o);
    }

}
