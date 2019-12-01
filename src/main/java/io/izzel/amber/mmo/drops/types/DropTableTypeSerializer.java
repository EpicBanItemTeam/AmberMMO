package io.izzel.amber.mmo.drops.types;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropTable;
import io.izzel.amber.mmo.drops.DropTableService;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.util.weighted.WeightedObject;

import java.util.Objects;

public class DropTableTypeSerializer implements TypeSerializer<DropTable> {

    @Nullable
    @Override
    public DropTable deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        if (!value.getChildrenMap().containsKey("weighted")) {
            int roll = value.getNode("roll").getInt(1);
            val builder = ImmutableList.<WeightedObject<DropTable>>builder();
            for (val item : value.getNode("items").getChildrenList()) {
                val iterator = item.getChildrenList().listIterator();
                if (iterator.hasNext()) {
                    val tableNode = iterator.next().getChildrenMap().entrySet().iterator().next();
                    val tableType = DropTableService.instance().getTypeById(tableNode.getKey().toString());
                    val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                    Objects.requireNonNull(table);
                    val weight = iterator.hasNext() ? iterator.next().getInt(1) : 1;
                    builder.add(new WeightedObject<>(table, weight));
                }
            }
            return new WeightedDropTable(builder.build(), roll);
        } else {
            val builder = ImmutableList.<DropTable>builder();
            for (val item : value.getNode("items").getChildrenList()) {
                val iterator = item.getChildrenList().listIterator();
                if (iterator.hasNext()) {
                    val tableNode = iterator.next().getChildrenMap().entrySet().iterator().next();
                    val tableType = DropTableService.instance().getTypeById(tableNode.getKey().toString());
                    val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                    Objects.requireNonNull(table);
                    val probability = iterator.hasNext() ? iterator.next().getDouble(1D) : 1D;
                    val amount = iterator.hasNext() ? iterator.next().getValue(TypeToken.of(Amount.class))
                        : new AmountSerializer.Fixed(1);
                    builder.add(new SimpleDropTableEntry(table, probability, amount));
                }
            }
            return new SimpleDropTable(builder.build());
        }
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable DropTable obj, @NonNull ConfigurationNode value) {
    }

}
