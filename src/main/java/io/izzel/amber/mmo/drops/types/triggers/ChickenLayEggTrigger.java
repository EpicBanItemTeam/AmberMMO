package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.DropTableService;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.animal.Chicken;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.PluginContainer;

public class ChickenLayEggTrigger implements DropTrigger {

    private EventListener<DropItemEvent.Pre> listener;

    @Override
    public void set(Runnable action) {
        if (listener == null) {
            listener = event -> {
                event.getCause().first(Entity.class).filter(Chicken.class::isInstance)
                    .ifPresent(entity -> {
                        if (event.getOriginalDroppedItems().get(0).getType().equals(ItemTypes.EGG)) {
                            try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                                DropContext context = new DropContext()
                                    .set(entity.getLocation(), DropContext.Key.LOCATION);
                                frame.pushCause(context);
                                action.run();
                                if (context.isOverrideDefault()) {
                                    event.getDroppedItems().clear();
                                }
                                DropTableService.instance().getDropItemProcessor().handle(context.getDrops());
                            }
                        }
                    });
            };
            Sponge.getEventManager().registerListener(
                Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new),
                DropItemEvent.Pre.class,
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

    public static class Serializer implements TypeSerializer<ChickenLayEggTrigger> {

        @Nullable
        @Override
        public ChickenLayEggTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
            return new ChickenLayEggTrigger();
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable ChickenLayEggTrigger obj, @NonNull ConfigurationNode value) {
        }
    }
}
