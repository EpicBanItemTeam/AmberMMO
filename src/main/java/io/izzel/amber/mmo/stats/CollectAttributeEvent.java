package io.izzel.amber.mmo.stats;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.event.GenericEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.TargetEntityEvent;

import java.util.Map;
import java.util.function.Function;

public interface CollectAttributeEvent<V> extends TargetEntityEvent, GenericEvent<V> {

    void setBaseValue(V value);

    V getBaseValue();

    V getFinalValue();

    void appendModifier(Cause cause, Function<V, V> function);

    void prependModifier(Cause cause, Function<V, V> function);

    Map<Cause, Function<V, V>> getModifiers();

    interface KeyValue<V> extends CollectAttributeEvent<V> {

        Key<? extends BaseValue<V>> getKey();

    }

    interface MagicValue<V> extends CollectAttributeEvent<V> {

        String getIdentifier();

    }

    interface Identifiers {
        String REGENERATION = "ambermmo:regeneration";
    }

}
