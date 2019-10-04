package io.izzel.amber.mmo.stats;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "DuplicatedCode"})
@Singleton
public class AmberStatsServiceImpl implements AmberStatsService {

    private final PluginContainer container;

    @Inject
    public AmberStatsServiceImpl(PluginContainer container, InternalPlayerStatsApplier applier) {
        this.container = container;
        Sponge.getServiceManager().setProvider(container, AmberStatsService.class, this);
        applier.registerAll();
    }

    @Override
    public <V> void registerGlobalKeyModifier(Key<? extends BaseValue<V>> key, Function<V, V> function) {
        Sponge.getEventManager().registerListener(container, (TypeToken<CollectAttributeEvent.KeyValue<V>>) TypeToken.of(new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{key.getElementToken().getType()};
            }

            @Override
            public Type getRawType() {
                return CollectAttributeEvent.KeyValue.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        }), event -> event.appendModifier(event.getCause().with(new Object()), function));
    }

    @Override
    public <V> void registerTemporaryModifier(Key<? extends BaseValue<V>> key, Function<V, V> function, long time, TimeUnit timeUnit) {
        EventListener<CollectAttributeEvent.KeyValue<V>> eventListener = event -> event.appendModifier(event.getCause().with(new Object()), function);
        Sponge.getEventManager().registerListener(container, (TypeToken<CollectAttributeEvent.KeyValue<V>>) TypeToken.of(new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{key.getElementToken().getType()};
            }

            @Override
            public Type getRawType() {
                return CollectAttributeEvent.KeyValue.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        }), eventListener);
        Task.builder().delay(time, timeUnit).execute(() -> Sponge.getEventManager().unregisterListeners(eventListener)).submit(this.container);
    }

    @Override
    public <V> void registerTemporaryModifier(Key<? extends BaseValue<V>> key, Function<V, V> function, Entity entity, long time, TimeUnit timeUnit) {
        UUID uniqueId = entity.getUniqueId();
        EventListener<CollectAttributeEvent.KeyValue<V>> eventListener = event -> {
            if (event.getTargetEntity().getUniqueId().equals(uniqueId)) {
                event.appendModifier(event.getCause().with(new Object()), function);
            }
        };
        Sponge.getEventManager().registerListener(container, (TypeToken<CollectAttributeEvent.KeyValue<V>>) TypeToken.of(new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{key.getElementToken().getType()};
            }

            @Override
            public Type getRawType() {
                return CollectAttributeEvent.KeyValue.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        }), eventListener);
        Task.builder().delay(time, timeUnit).execute(() -> Sponge.getEventManager().unregisterListeners(eventListener)).submit(this.container);
    }

    @Override
    public <V> V collectKeyValue(Entity entity, Key<? extends BaseValue<V>> key) {
        return collectKeyValue(entity, key, null);
    }

    @Override
    public <V> V collectKeyValue(Entity entity, Key<? extends BaseValue<V>> key, V baseValue) {
        TypeToken<?> elementToken = key.getElementToken();
        if (elementToken.equals(TypeToken.of(Double.class)))
            return (V) collectKeyValue(entity, (Key<? extends BaseValue<Double>>) key, baseValue == null ? 0D : (Double) baseValue, Double::sum);
        if (elementToken.equals(TypeToken.of(Integer.class)))
            return (V) collectKeyValue(entity, (Key<? extends BaseValue<Integer>>) key, baseValue == null ? 0 : (Integer) baseValue, Integer::sum);
        throw new IllegalArgumentException("No mergeFunction for " + key);
    }

    @Override
    public <V> V collectKeyValue(Entity entity, Key<? extends BaseValue<V>> key, V baseValue, BiFunction<V, V, V> mergeFunction) {
        try (final CauseStackManager.StackFrame stackFrame = Sponge.getCauseStackManager().pushCauseFrame()) {
            AbstractCollectAttributeEvent.KeyValueImpl<V> event = new AbstractCollectAttributeEvent.KeyValueImpl<>(baseValue, stackFrame.getCurrentCause(), entity, mergeFunction, key);
            Sponge.getEventManager().post(event);
            return event.getFinalValue();
        }
    }

    @Override
    public <V> V collectMagicValue(Entity entity, TypeToken<V> typeToken, String identifier, V baseValue, BiFunction<V, V, V> mergeFunction) {
        try (final CauseStackManager.StackFrame stackFrame = Sponge.getCauseStackManager().pushCauseFrame()) {
            AbstractCollectAttributeEvent.MagicValueImpl<V> event = new AbstractCollectAttributeEvent.MagicValueImpl<>(baseValue, stackFrame.getCurrentCause(), typeToken, entity, mergeFunction, identifier);
            Sponge.getEventManager().post(event);
            return event.getFinalValue();
        }
    }

}
