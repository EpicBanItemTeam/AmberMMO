package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class FishingTrigger implements DropTrigger {

    private EventListener<FishingEvent.Stop> listener;

    @Override
    public void set(Runnable action) {
        if (listener == null) {
            listener = event -> {
                Optional<Entity> optional = event.getCause().first(Entity.class);
                if (optional.isPresent()) {
                    Entity entity = optional.get();
                    try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                        DropContext context = new DropContext()
                            .set(entity, DropContext.Key.OWNER)
                            .set(entity.getLocation(), DropContext.Key.LOCATION);
                        frame.pushCause(context);
                        action.run();
                        if (context.isOverrideDefault()) {
                           event.getTransactions().clear();
                        }
                        for (ItemStackSnapshot snapshot : context.getDrops()) {
                            event.getTransactions().add(new Transaction<>(snapshot, snapshot));
                        }
                    }
                }
            };
            Sponge.getEventManager().registerListener(
                Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new),
                FishingEvent.Stop.class,
                Order.LAST,
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

    public static class Serializer implements TypeSerializer<FishingTrigger> {

        @Nullable
        @Override
        public FishingTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
            return new FishingTrigger();
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable FishingTrigger obj, @NonNull ConfigurationNode value) {
        }
    }

}
