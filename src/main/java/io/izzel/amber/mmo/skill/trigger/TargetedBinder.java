package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.op.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.util.OperateFunction;
import org.spongepowered.api.event.Event;

import java.util.function.Predicate;

public interface TargetedBinder<E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateFunction<O>> {

    TargetedBinder<E, C, O, F> when(Predicate<E> predicate);

    void to(F function);

}
