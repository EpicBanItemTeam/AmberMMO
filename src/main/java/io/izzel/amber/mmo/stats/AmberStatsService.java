package io.izzel.amber.mmo.stats;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface AmberStatsService {

    <V> void registerGlobalKeyModifier(Key<? extends BaseValue<V>> key, Function<V, V> function);

    <V> void registerTemporaryModifier(Key<? extends BaseValue<V>> key, Function<V, V> function, long time, TimeUnit timeUnit);

    <V> void registerTemporaryModifier(Key<? extends BaseValue<V>> key, Function<V, V> function, Entity entity, long time, TimeUnit timeUnit);

    <V> V collectKeyValue(Entity entity, Key<? extends BaseValue<V>> key);

    <V> V collectKeyValue(Entity entity, Key<? extends BaseValue<V>> key, V baseValue);

    <V> V collectKeyValue(Entity entity, Key<? extends BaseValue<V>> key, V baseValue, BiFunction<V, V, V> mergeFunction);

    <V> V collectMagicValue(Entity entity, TypeToken<V> typeToken, String identifier, V baseValue, BiFunction<V, V, V> mergeFunction);

    static AmberStatsService instance() {
        return Sponge.getServiceManager().provideUnchecked(AmberStatsService.class);
    }

}

