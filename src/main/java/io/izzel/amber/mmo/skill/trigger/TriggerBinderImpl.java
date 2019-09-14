package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.util.OperateFunction;
import org.spongepowered.api.event.Event;

class TriggerBinderImpl implements TriggerBinder {

    @Override
    public <E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateFunction<O>>
    TargetedBinder<E, C, O, F> bind(Trigger<E, C, O, F> trigger) {
        return new TargetedBinderImpl<>(trigger);
    }

}
