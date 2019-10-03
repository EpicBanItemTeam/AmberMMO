package io.izzel.amber.mmo.skill.skills.triggers;

import io.izzel.amber.mmo.skill.trigger.Trigger;
import io.izzel.amber.mmo.skill.trigger.dispatcher.Dispatcher3;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;

import java.util.Collection;

public final class Triggers {

    public static Trigger<MoveEntityEvent.Position, Dispatcher3<Entity, Collection<Entity>, Collection<Entity>>> lookingAt() {
        return new TriggerLookingAt();
    }

    public static <V> Trigger<ChangeDataHolderEvent.ValueChange, Dispatcher3<Entity, V, V>> dataChange(Key<Value<V>> key) {
        return new TriggerDataValueChange<>(key);
    }

    public static Trigger<ChangeDataHolderEvent.ValueChange, Dispatcher3<Entity, Boolean, Boolean>> toggleSneak() {
        return new TriggerDataValueChange<>(Keys.IS_SNEAKING);
    }

    public static Trigger<ChangeDataHolderEvent.ValueChange, Dispatcher3<Entity, Boolean, Boolean>> toggleSprint() {
        return new TriggerDataValueChange<>(Keys.IS_SPRINTING);
    }

}
