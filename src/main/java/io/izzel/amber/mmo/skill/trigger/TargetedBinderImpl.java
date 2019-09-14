package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.op.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.util.OperateFunction;
import lombok.extern.slf4j.Slf4j;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
final class TargetedBinderImpl<E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateFunction<O>>
    implements TargetedBinder<E, C, O, F> {

    private final Trigger<E, C, O, F> trigger;
    private Predicate<E> predicate = e -> true;

    TargetedBinderImpl(Trigger<E, C, O, F> trigger) {
        this.trigger = trigger;
    }

    @Override
    public TargetedBinder<E, C, O, F> when(Predicate<E> predicate) {
        this.predicate = this.predicate.and(predicate);
        return this;
    }

    @Override
    public void to(F function) {
        Optional<PluginContainer> optional = Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class);
        if (optional.isPresent()) {
            PluginContainer container = optional.get();
            Sponge.getEventManager().registerListener(container, trigger.getEventClass(), trigger.getListener(predicate, function));
        } else {
            log.warn("No PluginContainer found while binding triggers");
        }
    }

}
