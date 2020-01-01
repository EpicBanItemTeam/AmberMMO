package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;

public class ExpEntry implements DropTable {

    private final Amount amount;

    public ExpEntry(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void accepts() {
        DropContext context = DropContext.current();
        context.get(DropContext.Key.LOCATION).ifPresent(location -> {
            int d = (int) amount.get();
            if (d > 0) {
                Sponge.getCauseStackManager().addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.EXPERIENCE);
                Entity entity = location.getExtent().createEntityNaturally(EntityTypes.EXPERIENCE_ORB, location.getPosition());
                entity.offer(Keys.CONTAINED_EXPERIENCE, d);
                location.getExtent().spawnEntity(entity);
            }
        });
    }

    public static class Serializer implements TypeSerializer<ExpEntry> {

        @Nullable
        @Override
        public ExpEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            Amount amount = value.getValue(TypeToken.of(Amount.class), Amount.fixed(0));
            return new ExpEntry(amount);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable ExpEntry obj, @NonNull ConfigurationNode value) {
        }
    }
}
