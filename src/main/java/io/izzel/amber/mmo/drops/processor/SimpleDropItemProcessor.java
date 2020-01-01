package io.izzel.amber.mmo.drops.processor;

import com.google.inject.Inject;
import io.izzel.amber.mmo.drops.DropContext;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.List;

public class SimpleDropItemProcessor implements DropItemProcessor {

    @Inject private PluginContainer container;

    @Override
    public void handle(List<ItemStackSnapshot> list) {
        if (!list.isEmpty()) {
            DropContext context = DropContext.current();
            context.get(DropContext.Key.LOCATION).ifPresent(location -> {
                Task.builder().delayTicks(10) // todo fixed delay
                    .execute(() -> {
                        Sponge.getCauseStackManager().addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.DROPPED_ITEM);
                        for (ItemStackSnapshot snapshot : list) {
                            Entity entity = location.getExtent().createEntityNaturally(EntityTypes.ITEM, location.getPosition());
                            entity.offer(Keys.REPRESENTED_ITEM, snapshot);
                            location.getExtent().spawnEntity(entity);
                        }
                    }).submit(container);
            });
        }
    }

}
