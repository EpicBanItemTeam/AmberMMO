package io.izzel.amber.mmo.skill.skills.triggers;

import io.izzel.amber.mmo.skill.trigger.AbstractTrigger;
import io.izzel.amber.mmo.skill.trigger.dispatcher.Dispatcher3;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.entity.SneakingData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;

import java.util.Optional;

public class TriggerDataValueChange<V> extends AbstractTrigger<ChangeDataHolderEvent.ValueChange, Dispatcher3<Entity, V, V>> {

    private final Key<Value<V>> key;

    public TriggerDataValueChange(Key<Value<V>> key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EventListener<ChangeDataHolderEvent.ValueChange> getListener(Dispatcher3<Entity, V, V> dispatcher) {
        return event -> {
            event.getCause().first(Entity.class)
                .filter(it -> it.supports(SneakingData.class))
                .ifPresent(entity -> {
                    DataTransactionResult result = event.getEndResult();
                    Optional<ImmutableValue<V>> any = (Optional) result.getReplacedData().stream().filter(it -> it.getKey().equals(key)).findAny();
                    if (any.isPresent()) {
                        V prev = any.get().get();
                        result.getSuccessfulData()
                            .stream()
                            .filter(it -> it.getKey().equals(key))
                            .map(it -> (Value<V>) it)
                            .findAny().ifPresent(cur -> dispatcher.apply(entity, prev, cur.get()));
                    }
                });
        };
    }
}
