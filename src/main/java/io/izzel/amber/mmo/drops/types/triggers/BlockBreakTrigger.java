package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.DropTableService;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

public class BlockBreakTrigger implements DropTrigger {

    private final BlockType blockType;

    private Inner inner;

    public BlockBreakTrigger(BlockType blockType) {
        this.blockType = blockType;
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
            .add("blockType", blockType)
            .toString();
    }

    public class Inner {

        private final Runnable action;

        private Inner(Runnable action) {
            this.action = action;
        }

        private Set<Object> set = Collections.newSetFromMap(new WeakHashMap<>());

        @Listener(order = Order.LAST)
        public void on(ChangeBlockEvent.Break event) {
            set.clear();
            Entity entity = (Entity) event.getContext().get(EventContextKeys.OWNER).orElseThrow(NullPointerException::new);
            try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                DropContext context = new DropContext().set(entity, DropContext.Key.OWNER);
                frame.pushCause(context);
                event.getTransactions().stream()
                    .filter(Transaction::isValid)
                    .filter(it -> it.getOriginal().getState().getType() == blockType)
                    .map(Transaction::getOriginal)
                    .forEach(snapshot -> {
                        context.resetDrops();
                        context.set(snapshot.getState(), DropContext.Key.BLOCK)
                            .set(snapshot.getLocation(), DropContext.Key.LOCATION);
                        action.run();
                        if (context.isOverrideDefault()) {
                            set.add(snapshot);
                        }
                        DropTableService.instance().getDropItemProcessor().handle(context.getDrops());
                    });
            }
        }

        @Listener(order = Order.FIRST)
        public void on(DropItemEvent.Destruct event) {
            if (set.contains(event.getCause().root())) {
                event.setCancelled(true);
            }
        }

    }

    public static class Serializer implements TypeSerializer<BlockBreakTrigger> {

        @Nullable
        @Override
        public BlockBreakTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
            String string = value.getNode("type").getString();
            if (string != null) {
                Optional<BlockType> typeOptional = Sponge.getRegistry().getType(BlockType.class, string);
                if (typeOptional.isPresent()) {
                    return new BlockBreakTrigger(typeOptional.get());
                }
            }
            return null;
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable BlockBreakTrigger obj, @NonNull ConfigurationNode value) {
        }
    }
}
