package io.izzel.amber.mmo.drops.types.conditions;

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
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NonnullByDefault
public class DropCooldownData {

    public static class Mutable extends AbstractData<Mutable, Immutable> {

        private final Map<String, Long> map = new HashMap<>();

        public void put(String key, long value) {
            this.map.put(key, value);
        }

        public long get(String key) {
            return map.getOrDefault(key, 0L);
        }

        @Override
        protected void registerGettersAndSetters() {
        }

        @Override
        public Optional<Mutable> fill(DataHolder dataHolder, MergeFunction overlap) {
            Mutable original = dataHolder.get(Mutable.class).orElse(null);
            Mutable merge = overlap.merge(original, this);
            merge.map.forEach(this.map::put);
            return Optional.of(this);
        }

        @Override
        public Optional<Mutable> from(DataContainer container) {
            return Optional.of(fromContainer(container, this));
        }

        @Override
        public Mutable copy() {
            Mutable mutable = new Mutable();
            mutable.map.putAll(this.map);
            return mutable;
        }

        @Override
        public Immutable asImmutable() {
            Immutable immutable = new Immutable();
            immutable.map.putAll(this.map);
            return immutable;
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        protected DataContainer fillContainer(DataContainer dataContainer) {
            return DropCooldownData.fillContainer(super.fillContainer(dataContainer), map);
        }
    }

    public static class Immutable extends AbstractImmutableData<Immutable, Mutable> {

        private final Map<String, Long> map = new HashMap<>();

        @Override
        protected void registerGetters() {

        }

        @Override
        public Mutable asMutable() {
            Mutable mutable = new Mutable();
            mutable.map.putAll(this.map);
            return mutable;
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        protected DataContainer fillContainer(DataContainer dataContainer) {
            return DropCooldownData.fillContainer(super.fillContainer(dataContainer), map);
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

    private static final DataQuery MAP = DataQuery.of("Map");

    @SuppressWarnings("unchecked")
    private static Mutable fromContainer(DataView container, Mutable data) {
        container.getMap(MAP).ifPresent(it -> data.map.putAll((Map<? extends String, ? extends Long>) it));
        return data;
    }

    private static DataContainer fillContainer(DataContainer container, Map<String, Long> map) {
        container.set(MAP, map);
        return container;
    }

    public static long get(Entity entity, String key) {
        return entity.getOrCreate(Mutable.class).orElseThrow(IllegalStateException::new).get(key);
    }

    public static void update(Entity entity, String key, long value) {
        Mutable mutable = entity.getOrCreate(Mutable.class).orElseThrow(IllegalStateException::new);
        mutable.put(key, value);
        entity.offer(mutable);
    }
}
