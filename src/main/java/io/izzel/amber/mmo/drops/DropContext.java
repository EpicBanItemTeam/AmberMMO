package io.izzel.amber.mmo.drops;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.*;

@SuppressWarnings("unchecked")
public class DropContext {

    private final Map<Object, Object> map = new HashMap<>();
    private final LinkedList<ItemStackSnapshot> drops = new LinkedList<>();
    private boolean overrideDefault = false;

    public <T> DropContext set(T value, Key<? super T>... keys) {
        for (Key<? super T> key : keys) {
            map.put(key, value);
        }
        return this;
    }

    public DropContext set(Object value, String... keys) {
        for (String key : keys) {
            map.put(key, value);
        }
        return this;
    }

    public <T> Optional<T> get(Key<T> key) {
        return Optional.ofNullable((T) map.get(key));
    }

    public <T> Optional<T> get(String key) {
        return Optional.ofNullable((T) map.get(key));
    }

    public void addDrop(ItemStackSnapshot item) {
        this.drops.addLast(item);
    }

    public void resetDrops() {
        this.drops.clear();
    }

    public List<ItemStackSnapshot> getDrops() {
        return Collections.unmodifiableList(drops);
    }

    public void setOverrideDefault() {
        this.overrideDefault = true;
    }

    public boolean isOverrideDefault() {
        return overrideDefault;
    }

    public static class Key<T> {

        public static final Key<Entity> OWNER = new Key<>("owner");

        private final String key;

        public Key(String key) {
            this.key = key;
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
            return key.equals(o);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

    }

}
