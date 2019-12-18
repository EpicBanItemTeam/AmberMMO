package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class EntityKillTrigger implements DropTrigger {

    private final EntityType entityType;

    private EventListener<DestructEntityEvent.Death> listener;

    public EntityKillTrigger(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public void set(Runnable action) {
        if (listener == null) {
            listener = event -> {
                if (!event.isCancelled() && event.getSource() instanceof EntityDamageSource) {
                    EntityDamageSource source = (EntityDamageSource) event.getSource();
                    Entity entity = event.getTargetEntity();
                    if (entity.getType() == entityType) {
                        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                            frame.pushCause(new DropContext()
                                .set(source.getSource(), DropContext.Key.OWNER)
                                .set(entity, DropContext.Key.DAMAGEE)
                                .set(entity.getLocation(), DropContext.Key.LOCATION));
                            action.run();
                        }
                    }
                }
            };
            Sponge.getEventManager().registerListener(
                Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new),
                DestructEntityEvent.Death.class,
                Order.BEFORE_POST,
                listener
            );
        } else throw new IllegalStateException();
    }

    @Override
    public void unset() {
        if (listener != null) {
            Sponge.getEventManager().unregisterListeners(listener);
            listener = null;
        } else throw new IllegalStateException();
    }

    public static class Serializer implements TypeSerializer<EntityKillTrigger> {

        @Nullable
        @Override
        public EntityKillTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
            String string = value.getNode("type").getString();
            if (string == null) return new EntityKillTrigger(null);
            else {
                Optional<EntityType> typeOptional = Sponge.getRegistry().getType(EntityType.class, string);
                if (typeOptional.isPresent()) {
                    return new EntityKillTrigger(typeOptional.get());
                }
            }
            return null;
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable EntityKillTrigger obj, @NonNull ConfigurationNode value) {
        }
    }
}
