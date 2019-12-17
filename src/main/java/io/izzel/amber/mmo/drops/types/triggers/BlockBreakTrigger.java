package io.izzel.amber.mmo.drops.types.triggers;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class BlockBreakTrigger implements DropTrigger {

    private final BlockType blockType;

    private EventListener<DropItemEvent.Destruct> listener;

    public BlockBreakTrigger(BlockType blockType) {
        this.blockType = blockType;
    }

    @Override
    public void set(Runnable action) {
        if (listener == null) {
            listener = event -> {
                if (event.getSource() instanceof BlockSnapshot) {
                    BlockSnapshot snapshot = ((BlockSnapshot) event.getSource());
                    event.getContext().get(EventContextKeys.OWNER).filter(Entity.class::isInstance).map(Entity.class::cast).ifPresent(entity -> {
                        if (blockType == null || snapshot.getState().getType() == blockType) {
                            try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                                frame.pushCause(new DropContext()
                                    .set(snapshot.getLocation(), DropContext.Key.LOCATION)
                                    .set(snapshot.getState(), DropContext.Key.BLOCK)
                                    .set(entity, DropContext.Key.OWNER));
                                action.run();
                            }
                        }
                    });
                }
            };
            Sponge.getEventManager().registerListener(
                Sponge.getCauseStackManager().getCurrentCause().first(PluginContainer.class).orElseThrow(IllegalStateException::new),
                DropItemEvent.Destruct.class,
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
        public BlockBreakTrigger deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            String string = value.getNode("type").getString();
            if (string == null) return new BlockBreakTrigger(null);
            else {
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
