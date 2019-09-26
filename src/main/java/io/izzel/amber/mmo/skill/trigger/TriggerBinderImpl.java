package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.dispatcher.OperateDispatcher;
import org.spongepowered.api.event.Event;

class TriggerBinderImpl implements TriggerBinder {

    @Override
    public <E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateDispatcher>
    TargetedBinder<E, C, O, F> bind(Trigger<E, F> trigger) {
        return new TargetedBinderImpl<>(trigger);
    }

}
