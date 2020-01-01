package io.izzel.amber.mmo.drops.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import lombok.val;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Coerce;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

@NonnullByDefault
public class DropPlayerData {

    public static class Mutable extends AbstractData<Mutable, Immutable> {

        private final Map<String, Long> cooldown = new HashMap<>();
        private final Multimap<String, AmountTempModifier> tempModifier = MultimapBuilder.hashKeys().arrayListValues().build();

        public void putCooldown(String key, long value) {
            this.cooldown.put(key, value);
        }

        public long getCooldown(String key) {
            return cooldown.getOrDefault(key, 0L);
        }

        @Override
        protected void registerGettersAndSetters() {
        }

        @Override
        public Optional<Mutable> fill(DataHolder dataHolder, MergeFunction overlap) {
            Mutable original = dataHolder.get(Mutable.class).orElse(null);
            Mutable merge = overlap.merge(original, this);
            this.cooldown.putAll(merge.cooldown);
            this.tempModifier.putAll(merge.tempModifier);
            return Optional.of(this);
        }

        @Override
        public Optional<Mutable> from(DataContainer container) {
            return Optional.of(fromContainer(container, this));
        }

        @Override
        public Mutable copy() {
            Mutable mutable = new Mutable();
            mutable.cooldown.putAll(this.cooldown);
            mutable.tempModifier.putAll(this.tempModifier);
            return mutable;
        }

        @Override
        public Immutable asImmutable() {
            Immutable immutable = new Immutable();
            immutable.cooldown.putAll(this.cooldown);
            immutable.tempModifier.putAll(this.tempModifier);
            return immutable;
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        protected DataContainer fillContainer(DataContainer dataContainer) {
            return DropPlayerData.fillContainer(super.fillContainer(dataContainer), cooldown, tempModifier);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("cooldown", cooldown)
                .add("tempModifier", tempModifier)
                .toString();
        }
    }

    public static class Immutable extends AbstractImmutableData<Immutable, Mutable> {

        private final Map<String, Long> cooldown = new HashMap<>();
        private final Multimap<String, AmountTempModifier> tempModifier = MultimapBuilder.hashKeys().arrayListValues().build();

        @Override
        protected void registerGetters() {

        }

        @Override
        public Mutable asMutable() {
            Mutable mutable = new Mutable();
            mutable.cooldown.putAll(this.cooldown);
            mutable.tempModifier.putAll(this.tempModifier);
            return mutable;
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        protected DataContainer fillContainer(DataContainer dataContainer) {
            return DropPlayerData.fillContainer(super.fillContainer(dataContainer), cooldown, tempModifier);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("cooldown", cooldown)
                .add("tempModifier", tempModifier)
                .toString();
        }
    }

    public static class Builder extends AbstractDataBuilder<Mutable> implements DataManipulatorBuilder<Mutable, Immutable> {

        public Builder() {
            super(Mutable.class, 0);
        }

        @Override
        public Mutable create() {
            return new Mutable();
        }

        @Override
        public Optional<Mutable> createFrom(DataHolder dataHolder) {
            return this.create().fill(dataHolder);
        }

        @Override
        protected Optional<Mutable> buildContent(DataView container) throws InvalidDataException {
            return Optional.of(fromContainer(container, this.create()));
        }
    }

    private static final DataQuery COOLDOWN = DataQuery.of("Cooldown");
    private static final DataQuery MODIFIER = DataQuery.of("TempModifier");

    @SuppressWarnings("unchecked")
    private static Mutable fromContainer(DataView container, Mutable data) {
        container.getMap(COOLDOWN).ifPresent(it -> data.cooldown.putAll((Map<? extends String, ? extends Long>) it));
        container.getMap(MODIFIER).ifPresent(it -> {
            for (val entry : ((Map<String, List<Map<String, ?>>>) it).entrySet()) {
                for (Map<String, ?> map : entry.getValue()) {
                    long timeout = Coerce.toLong(map.get("Timeout"));
                    String expression = String.valueOf(map.get("Expression"));
                    data.tempModifier.put(entry.getKey(), new AmountTempModifier(timeout, expression));
                }
            }
        });
        return data;
    }

    private static DataContainer fillContainer(DataContainer container, Map<String, Long> cooldown, Multimap<String, AmountTempModifier> tempModifier) {
        container.set(COOLDOWN, cooldown);
        container.set(MODIFIER, tempModifier.asMap());
        return container;
    }

    public static long getCooldown(Entity entity, String key) {
        return entity.getOrCreate(Mutable.class).orElseThrow(IllegalStateException::new).getCooldown(key);
    }

    public static void updateCooldown(Entity entity, String key, long value) {
        Mutable mutable = entity.getOrCreate(Mutable.class).orElseThrow(IllegalStateException::new);
        mutable.putCooldown(key, value);
        entity.offer(mutable);
    }

    public static DoubleUnaryOperator getModifiers(Entity entity, String key) {
        Mutable mutable = entity.getOrCreate(Mutable.class).orElseThrow(IllegalStateException::new);
        DoubleUnaryOperator ret = DoubleUnaryOperator.identity();
        val iterator = mutable.tempModifier.get(key).iterator();
        while (iterator.hasNext()) {
            AmountTempModifier modifier = iterator.next();
            if (System.currentTimeMillis() > modifier.getTimeout()) iterator.remove();
            else ret = ret.andThen(modifier.getOperator());
        }
        return ret;
    }

    public static void addModifier(Entity entity, String key, AmountTempModifier modifier) {
        Mutable mutable = entity.getOrCreate(Mutable.class).orElseThrow(IllegalStateException::new);
        mutable.tempModifier.put(key, modifier);
        entity.offer(mutable);
    }

}
