package io.izzel.amber.mmo.drops;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

@SuppressWarnings("unchecked")
public class DropContext {

    private final Map<Key<?>, Object> keyMap = new WeakHashMap<>();
    private final Map<String, Object> strMap = new WeakHashMap<>();
    private final LinkedList<ItemStackSnapshot> drops = new LinkedList<>();
    private boolean overrideDefault = false;

    public <T> DropContext set(T value, Key<? super T> key) {
        keyMap.put(key, value);
        strMap.put(key.key, value);
        return this;
    }

    public <T> DropContext set(T value, Key<? super T> key, Key<?>... keys) {
        set(value, key);
        for (Key<?> k : keys) {
            keyMap.put(k, value);
            strMap.put(k.key, value);
        }
        return this;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public <T> DropContext set(Optional<T> value, Key<? super T> key, Key<?>... keys) {
        value.ifPresent(t -> set(t, key, keys));
        return this;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public <T> DropContext set(Optional<T> value, Key<? super T> key) {
        value.ifPresent(t -> set(t, key));
        return this;
    }

    public <T> Optional<T> get(Key<T> key) {
        return Optional.ofNullable((T) keyMap.get(key));
    }

    public <T> Optional<T> get(String key) {
        return Optional.ofNullable((T) strMap.get(key));
    }

    public void addDrop(ItemStackSnapshot item) {
        this.drops.addLast(item);
    }

    public void resetDrops() {
        this.drops.clear();
        this.overrideDefault = false;
    }

    public void resetContext() {
        this.strMap.clear();
        this.keyMap.clear();
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

    public static final class Key<T> {

        public static final Key<Entity> OWNER = new Key<>("owner");
        public static final Key<Location<World>> LOCATION = new Key<>("location");
        public static final Key<BlockState> BLOCK = new Key<>("block");
        public static final Key<Entity> DAMAGEE = new Key<>("damagee");

        private final String key;

        public Key(String key) {
            this.key = key;
        }

    }

}
