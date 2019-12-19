package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.data.DropPlayerData;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CooldownCondition extends DropCondition {

    class Serializer implements TypeSerializer<CooldownCondition> {

        @Nullable
        @Override
        public CooldownCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            val node = value.getChildrenMap().entrySet().iterator().next();
            val nodeType = node.getKey().toString();
            if (nodeType.equalsIgnoreCase("global")) {
                val tick = node.getValue().getNode("tick").getValue(TypeToken.of(Amount.class));
                return new Global(tick);
            } else if (nodeType.equalsIgnoreCase("player")) {
                val tick = node.getValue().getNode("tick").getValue(TypeToken.of(Amount.class));
                val id = node.getValue().getNode("id").getString(joinPath(value));
                return new Player(tick, id);
            }
            return null;
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable CooldownCondition obj, @NonNull ConfigurationNode value) {
        }

        private static String joinPath(ConfigurationNode node) {
            return Arrays.stream(node.getPath()).map(Objects::toString).collect(Collectors.joining("."));
        }

        private static class Global implements CooldownCondition {

            private final Amount tick;
            private long last = 0;

            private Global(Amount tick) {
                this.tick = tick;
            }

            @Override
            public boolean test() {
                int d = (int) (tick.get() * 50d);
                if (System.currentTimeMillis() - last > d) {
                    last = System.currentTimeMillis();
                    return true;
                }
                return false;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("tick", tick)
                    .toString();
            }
        }

        private static class Player implements CooldownCondition {

            private final Amount tick;
            private final String id;

            private Player(Amount tick, String id) {
                this.tick = tick;
                this.id = id;
            }

            @Override
            public boolean test() {
                Optional<DropContext> contextOptional = Sponge.getCauseStackManager().getCurrentCause().last(DropContext.class);
                if (contextOptional.isPresent()) {
                    DropContext context = contextOptional.get();
                    Optional<Entity> entityOptional = context.get(DropContext.Key.OWNER);
                    if (entityOptional.isPresent()) {
                        Entity entity = entityOptional.get();
                        long time = (long) (tick.get() * 20D);
                        long last = DropPlayerData.getCooldown(entity, id);
                        if (System.currentTimeMillis() - last > time) {
                            DropPlayerData.updateCooldown(entity, id, System.currentTimeMillis());
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("tick", tick)
                    .add("id", id)
                    .toString();
            }
        }

    }

}
