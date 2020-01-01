package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.base.MoreObjects;
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
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class VanillaEntry implements DropTable {

    private final Amount amount;
    private final ItemType type;

    public VanillaEntry(String id, Amount amount) throws ObjectMappingException {
        this.amount = amount;
        this.type = Sponge.getRegistry().getType(ItemType.class, id).orElseThrow(ObjectMappingException::new);
    }

    @Override
    public void accepts() {
        Optional<DropContext> optional = Sponge.getCauseStackManager().getCurrentCause().first(DropContext.class);
        if (optional.isPresent()) {
            DropContext context = optional.get();
            context.addDrop(ItemStack.builder().itemType(type).quantity(((int) amount.get())).build().createSnapshot());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("type", type)
            .add("amount", amount)
            .toString();
    }

    public static class Serializer implements TypeSerializer<VanillaEntry> {

        @Nullable
        @Override
        public VanillaEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            String id = value.getNode("id").getString();
            Amount amount = value.getNode("amount").getValue(TypeToken.of(Amount.class), Amount.fixed(1));
            if (id == null) throw new ObjectMappingException();
            else return new VanillaEntry(id, amount);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable VanillaEntry obj, @NonNull ConfigurationNode value) {
        }
    }

}
