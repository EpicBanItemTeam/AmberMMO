package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.util.OperateFunction;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;

import java.util.function.Predicate;

public interface Trigger<E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateFunction<O>> {

    Class<E> getEventClass();

    EventListener<E> getListener(Predicate<E> predicate, F function);

}
