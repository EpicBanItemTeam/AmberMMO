package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.dispatcher.OperateDispatcher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;
import java.util.function.Predicate;

final class TargetedBinderImpl<E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateDispatcher>
    implements TargetedBinder<E, C, O, F> {

    private final Trigger<E, F> trigger;
    private Predicate<E> predicate = e -> true;

    TargetedBinderImpl(Trigger<E, F> trigger) {
        this.trigger = trigger;
    }

    @Override
    public TargetedBinder<E, C, O, F> when(Predicate<E> predicate) {
        this.predicate = this.predicate.and(predicate);
        return this;
    }

    @Override
    public void to(F dispatcher) {
        Optional<PluginContainer> optional = Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class);
        if (optional.isPresent()) {
            PluginContainer container = optional.get();
            EventListener<E> listener = trigger.getListener(dispatcher);
            Sponge.getEventManager().registerListener(container, trigger.getEventClass(), event -> {
                if (predicate.test(event)) {
                    listener.handle(event);
                }
            });
        } else {
            throw new IllegalStateException("No PluginContainer found while binding triggers");
        }
    }

}
