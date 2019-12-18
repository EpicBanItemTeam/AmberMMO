package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class BlockBreakTrigger implements DropTrigger {

    private final BlockType blockType;

    private EventListener<ChangeBlockEvent.Break> listener;

    public BlockBreakTrigger(BlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public void set(Runnable action) {
        if (listener == null) {
            listener = event -> {
                event.getContext().get(EventContextKeys.OWNER).filter(Entity.class::isInstance).map(Entity.class::cast).ifPresent(entity -> {
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
                            });
                    }
                });
            };
            Sponge.getEventManager().registerListener(
                Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new),
                ChangeBlockEvent.Break.class,
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
