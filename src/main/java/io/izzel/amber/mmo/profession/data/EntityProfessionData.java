package io.izzel.amber.mmo.profession.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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

import java.util.*;

@NonnullByDefault
public class EntityProfessionData {

    public static class Mutable extends AbstractData<Mutable, Immutable> {

        private final Map<String, MutableProfession> map;

        public Mutable(Map<String, MutableProfession> map) {
            this.map = Maps.newHashMap(map);
        }

        public Map<String, MutableProfession> getProfessions() {
            return Collections.unmodifiableMap(map);
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
            return new Mutable(map);
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
            return EntityProfessionData.fillContainer(super.fillContainer(dataContainer), map);
        }
    }

    public static class Immutable extends AbstractImmutableData<Immutable, Mutable> {

        private final Map<String, MutableProfession> map;

        public Immutable(Map<String, MutableProfession> map) {
            this.map = ImmutableMap.copyOf(map);
        }

        public Map<String, MutableProfession> getProfessions() {
            return Collections.unmodifiableMap(map);
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
            return EntityProfessionData.fillContainer(super.fillContainer(dataContainer), map);
        }
    }

    public static class Builder extends AbstractDataBuilder<Mutable> implements DataManipulatorBuilder<Mutable, Immutable> {

        public Builder() {
            super(Mutable.class, 0);
        }

        @Override
        public Mutable create() {
            return new Mutable(new HashMap<>());
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

    private static DataQuery PROF = DataQuery.of("Profession");

    private static DataContainer fillContainer(DataContainer container, Map<String, MutableProfession> map) {
        container.set(PROF, ImmutableList.copyOf(map.values()));
        return container;
    }

    private static Mutable fromContainer(DataView container, Mutable data) {
        Optional<List<MutableProfession>> list = container.getSerializableList(PROF, MutableProfession.class);
        if (list.isPresent()) {
            List<MutableProfession> professions = list.get();
            for (MutableProfession profession : professions) {
                data.map.put(profession.getId(), profession);
            }
        }
        return data;
    }

}
