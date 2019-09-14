package io.izzel.amber.mmo.skill.trigger;

import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.util.OperateFunction;
import org.spongepowered.api.event.Event;

public abstract class AbstractTriggerModule implements TriggerModule {

    private TriggerBinder binder;

    @SuppressWarnings("WeakerAccess")
    protected abstract void configure();

    protected <E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateFunction<O>>
    TargetedBinder<E, C, O, F> bind(Trigger<E, C, O, F> trigger) {
        return this.binder.bind(trigger);
    }

    @Override
    public final void configure(TriggerBinder binder) {
        Preconditions.checkState(this.binder == null);
        this.binder = Preconditions.checkNotNull(binder);
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }
}
