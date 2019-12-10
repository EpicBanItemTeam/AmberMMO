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

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class DropTableTypeSerializer implements TypeSerializer<DropTable> {

    @Nullable
    @Override
    public DropTable deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        val path = joinPath(value);
        int roll = value.getNode("roll").getInt(1);
        if (!value.getChildrenMap().containsKey("weighted")) {
            val builder = ImmutableList.<WeightedObject<DropTable>>builder();
            for (val item : value.getNode("items").getChildrenList()) {
                val iterator = item.getChildrenList().listIterator();
                if (iterator.hasNext()) {
                    val tableNode = iterator.next().getChildrenMap().entrySet().iterator().next();
                    val tableType = DropTableService.instance().getTypeById(tableNode.getKey().toString());
                    val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                    Objects.requireNonNull(table);
                    val weight = iterator.hasNext() ? iterator.next().getValue(TypeToken.of(Amount.class)) : Amount.fixed(1);
                    builder.add(new DynamicWeightedObject<>(table, weight));
                }
            }
            return new WeightedDropTable(path, builder.build(), roll);
        } else {
            val builder = ImmutableList.<DropTable>builder();
            for (val item : value.getNode("items").getChildrenList()) {
                val iterator = item.getChildrenList().listIterator();
                if (iterator.hasNext()) {
                    val index = iterator.nextIndex();
                    val tableNode = iterator.next().getChildrenMap().entrySet().iterator().next();
                    val tableType = DropTableService.instance().getTypeById(tableNode.getKey().toString());
                    val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                    Objects.requireNonNull(table);
                    val probability = iterator.hasNext() ? iterator.next().getDouble(1D) : 1D;
                    val amount = iterator.hasNext() ? iterator.next().getValue(TypeToken.of(Amount.class))
                        : new AmountSerializer.Fixed(1);
                    builder.add(new SimpleDropTableEntry(String.format("%s.%d", path, index), table, probability, amount));
                }
            }
            return new SimpleDropTable(path, builder.build(), roll);
        }
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable DropTable obj, @NonNull ConfigurationNode value) {
    }

    private static String joinPath(ConfigurationNode node) {
        return Arrays.stream(node.getPath()).map(Objects::toString).collect(Collectors.joining("."));
    }

}
