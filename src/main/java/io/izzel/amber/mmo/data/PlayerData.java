package io.izzel.amber.mmo.data;

import com.google.common.collect.ImmutableMap;
import io.izzel.amber.mmo.MMOService;
import org.spongepowered.api.Sponge;
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
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NonnullByDefault
public class PlayerData {

    public static class Mutable extends AbstractData<Mutable, Immutable> {

        private final Map<String, Double> map;

        public Mutable(Map<String, Double> map) {
            this.map = map;
        }

        public double getAttribute(String id) {
            return map.get(id);
        }

        @Override
        protected void registerGettersAndSetters() {
        }

        @Override
        public Optional<Mutable> fill(DataHolder dataHolder, MergeFunction overlap) {
            Mutable mutable = dataHolder.get(Mutable.class).orElse(null);
            Mutable merge = overlap.merge(mutable, this);
            merge.map.forEach(map::put);
            return Optional.of(this);
        }

        @Override
        public Optional<Mutable> from(DataContainer container) {
            MMOService service = Sponge.getServiceManager().provideUnchecked(MMOService.class);
            for (String attribute : service.getAttributes()) {
                container.getDouble(DataQuery.of(attribute)).ifPresent(it -> map.put(attribute, it));
            }
            return Optional.of(this);
        }

        @Override
        public Mutable copy() {
            return new Mutable(new HashMap<>(map));
        }

        @Override
        public Immutable asImmutable() {
            return new Immutable(map);
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        protected DataContainer fillContainer(DataContainer dataContainer) {
            DataContainer container = super.fillContainer(dataContainer);
            map.forEach((k, v) -> container.set(DataQuery.of(k), v));
            return container;
        }
    }

    public static class Immutable extends AbstractImmutableData<Immutable, Mutable> {

        private final Map<String, Double> map;

        public Immutable(Map<String, Double> map) {
            this.map = ImmutableMap.copyOf(map);
        }

        @Override
        protected void registerGetters() {
        }

        @Override
        public Mutable asMutable() {
            return new Mutable(map);
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        protected DataContainer fillContainer(DataContainer dataContainer) {
            DataContainer container = super.fillContainer(dataContainer);
            map.forEach((k, v) -> container.set(DataQuery.of(k), v));
            return container;
        }
    }

    public static class Builder extends AbstractDataBuilder<Mutable> implements DataManipulatorBuilder<Mutable, Immutable> {

        public Builder() {
            super(Mutable.class, 0);
        }

        @Override
        protected Optional<Mutable> buildContent(DataView container) throws InvalidDataException {
            Mutable mutable = this.create();
            MMOService service = Sponge.getServiceManager().provideUnchecked(MMOService.class);
            for (String attribute : service.getAttributes()) {
                container.getDouble(DataQuery.of(attribute)).ifPresent(it -> mutable.map.put(attribute, it));
            }
            return Optional.of(mutable);
        }

        @Override
        public Mutable create() {
            return new Mutable(new HashMap<>());
        }

        @Override
        public Optional<Mutable> createFrom(DataHolder dataHolder) {
            return this.create().fill(dataHolder);
        }
    }
}
