package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.DropTableService;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

public class EntityKillTrigger implements DropTrigger {

    private final EntityType entityType;

    private Inner inner;

    public EntityKillTrigger(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public void set(Runnable action) {
        if (inner == null) {
            inner = new Inner(action);
            Sponge.getEventManager().registerListeners(
                Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new),
                inner
            );
        } else throw new IllegalStateException();
    }

    @Override
    public void unset() {
        if (inner != null) {
            Sponge.getEventManager().unregisterListeners(inner);
            inner = null;
        } else throw new IllegalStateException();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("entityType", entityType)
            .toString();
    }

    public class Inner {

        private final Runnable action;

        private Set<Entity> set = Collections.newSetFromMap(new WeakHashMap<>());

        private Inner(Runnable action) {
            this.action = action;
        }

        @Listener(order = Order.LAST)
        public void on(DestructEntityEvent.Death event) {
            set.clear();
            if (event.getSource() instanceof EntityDamageSource) {
                EntityDamageSource source = (EntityDamageSource) event.getSource();
                Entity entity = event.getTargetEntity();
                if (entity.getType() == entityType) {
                    try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                        DropContext context = new DropContext()
                            .set(source.getSource(), DropContext.Key.OWNER)
                            .set(entity, DropContext.Key.DAMAGEE)
                            .set(entity.getLocation(), DropContext.Key.LOCATION);
                        frame.pushCause(context);
                        action.run();
                        if (context.isOverrideDefault()) {
                            set.add(entity);
                        }
                        DropTableService.instance().getDropItemProcessor().handle(context.getDrops());
                    }
                }
            }
        }

        @Listener(order = Order.FIRST)
        public void on(DropItemEvent.Destruct event, @First Entity entity) {
            if (set.contains(entity)) {
                event.setCancelled(true);
            }
        }
    }

    public static class Serializer implements TypeSerializer<EntityKillTrigger> {

        @Nullable
        @Override
        public EntityKillTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            String string = value.getNode("type").getString();
            if (string == null) return new EntityKillTrigger(null);
            else {
                Optional<EntityType> typeOptional = Sponge.getRegistry().getType(EntityType.class, string);
                if (typeOptional.isPresent()) {
                    return new EntityKillTrigger(typeOptional.get());
                }
            }
            throw new ObjectMappingException();
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable EntityKillTrigger obj, @NonNull ConfigurationNode value) {
        }
    }
}
