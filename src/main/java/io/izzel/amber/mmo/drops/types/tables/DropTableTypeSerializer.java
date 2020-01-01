package io.izzel.amber.mmo.drops.types.tables;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropTableService;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import io.izzel.amber.mmo.drops.types.tables.internals.DropTableEntry;
import lombok.val;
import lombok.var;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.util.weighted.WeightedObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class DropTableTypeSerializer implements TypeSerializer<DropTable> {

    @SuppressWarnings("DuplicatedCode")
    @Nullable
    @Override
    public DropTable deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        if (value.getValueType() == ValueType.MAP) {
            val path = joinPath(value);
            int roll = value.getNode("roll").getInt(1);
            if (value.getChildrenMap().containsKey("weighted")) {
                val builder = ImmutableList.<WeightedObject<DropTable>>builder();
                var index = 0;
                for (val item : value.getNode("items").getChildrenList()) {
                    val iterator = item.getChildrenList().listIterator();
                    if (iterator.hasNext()) {
                        val subId = String.format("%s.%d", path, index);
                        val tableNode = iterator.next().getChildrenMap().entrySet().iterator().next();
                        val tableType = DropTableService.instance().getDropTableTypeById(tableNode.getKey().toString());
                        val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                        Objects.requireNonNull(table);
                        val weight = iterator.hasNext() ? iterator.next().getValue(TypeToken.of(Amount.class)) : Amount.fixed(1);
                        builder.add(new DynamicWeightedObject<>(subId, table, Objects.requireNonNull(weight)));
                    }
                    index++;
                }
                return new WeightedDropTable(path, builder.build(), roll);
            } else {
                val builder = ImmutableList.<DropTable>builder();
                var index = 0;
                for (val item : value.getNode("items").getChildrenList()) {
                    if (item.getValueType() == ValueType.LIST) {
                        val iterator = item.getChildrenList().listIterator();
                        if (iterator.hasNext()) {
                            val subId = String.format("%s.%d", path, index);
                            val tableNode = iterator.next().getChildrenMap().entrySet().iterator().next();
                            val tableType = DropTableService.instance().getDropTableTypeById(tableNode.getKey().toString());
                            val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                            Objects.requireNonNull(table);
                            val probability = iterator.hasNext() ? iterator.next().getValue(TypeToken.of(Amount.class)) : Amount.fixed(1);
                            val amount = iterator.hasNext() ? iterator.next().getValue(TypeToken.of(Amount.class)) : Amount.fixed(1);
                            builder.add(new ChanceTableEntry(subId, table, probability, amount));
                        }
                    } else if (item.getValueType() == ValueType.MAP) {
                        val subId = String.format("%s.%d", path, index);
                        val tableNode = item.getChildrenMap().entrySet().iterator().next();
                        val tableType = DropTableService.instance().getDropTableTypeById(tableNode.getKey().toString());
                        val table = tableNode.getValue().getValue(TypeToken.of(tableType));
                        builder.add(new ChanceTableEntry(subId, table, Amount.fixed(1), Amount.fixed(1)));
                    }
                    index++;
                }
                return new ChanceDropTable(path, builder.build(), roll);
            }
        } else if (value.getValueType() == ValueType.SCALAR) {
            return new DropTableEntry(Objects.requireNonNull(value.getValue()).toString());
        } else if (value.getValueType() == ValueType.LIST) {
            return new NestedDropTable(value.getList(TypeToken.of(DropTable.class)));
        } else throw new ObjectMappingException();
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable DropTable obj, @NonNull ConfigurationNode value) {
    }

    private static String joinPath(ConfigurationNode node) {
        return Arrays.stream(node.getPath()).map(Objects::toString).collect(Collectors.joining("."));
    }

}
