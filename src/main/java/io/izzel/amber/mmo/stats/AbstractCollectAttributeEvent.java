package io.izzel.amber.mmo.stats;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@NonnullByDefault
abstract class AbstractCollectAttributeEvent<V> implements CollectAttributeEvent<V> {

    private final LinkedList<Tuple<Cause, Function<V, V>>> list = new LinkedList<>();
    private final BiFunction<V, V, V> mergeFunction;
    private final Cause cause;
    private final TypeToken<V> typeToken;
    private final Entity targetEntity;

    private V baseValue;

    public AbstractCollectAttributeEvent(V value, Cause cause, TypeToken<V> typeToken, Entity targetEntity, BiFunction<V, V, V> mergeFunction) {
        this.baseValue = value;
        this.cause = cause;
        this.typeToken = typeToken;
        this.targetEntity = targetEntity;
        this.mergeFunction = mergeFunction;
    }

    @Override
    public void setBaseValue(V value) {
        this.baseValue = value;
    }

    @Override
    public V getBaseValue() {
        return baseValue;
    }

    @Override
    public V getFinalValue() {
        V ret = baseValue;
        for (Tuple<Cause, Function<V, V>> tuple : list) {
            ret = mergeFunction.apply(ret, tuple.getSecond().apply(ret));
        }
        return ret;
    }

    @Override
    public void appendModifier(Cause cause, Function<V, V> function) {
        list.addLast(Tuple.of(cause, function));
    }

    @Override
    public void prependModifier(Cause cause, Function<V, V> function) {
        list.addFirst(Tuple.of(cause, function));
    }

    @Override
    public Map<Cause, Function<V, V>> getModifiers() {
        return list.stream().collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
    }

    @Override
    public TypeToken<V> getGenericType() {
        return typeToken;
    }

    @Override
    public Entity getTargetEntity() {
        return targetEntity;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    @SuppressWarnings("unchecked")
    static final class KeyValueImpl<V> extends AbstractCollectAttributeEvent<V> implements KeyValue<V> {

        private final Key<? extends BaseValue<V>> key;

        public KeyValueImpl(V value, Cause cause, Entity targetEntity, BiFunction<V, V, V> mergeFunction, Key<? extends BaseValue<V>> key) {
            super(value, cause, (TypeToken<V>) key.getElementToken(), targetEntity, mergeFunction);
            this.key = key;
        }

        @Override
        public Key<? extends BaseValue<V>> getKey() {
            return key;
        }
    }

    static final class MagicValueImpl<V> extends AbstractCollectAttributeEvent<V> implements MagicValue<V> {

        private final String identifier;

        public MagicValueImpl(V value, Cause cause, TypeToken<V> typeToken, Entity targetEntity, BiFunction<V, V, V> mergeFunction, String identifier) {
            super(value, cause, typeToken, targetEntity, mergeFunction);
            this.identifier = identifier;
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }
    }

}
