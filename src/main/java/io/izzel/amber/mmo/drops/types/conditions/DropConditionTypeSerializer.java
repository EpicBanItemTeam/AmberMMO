package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropTableService;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DropConditionTypeSerializer implements TypeSerializer<DropCondition> {

    @Nullable
    @Override
    public DropCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        val node = value.getChildrenMap().entrySet().iterator().next();
        val nodeType = DropTableService.instance().getDropConditionTypeById(node.getKey().toString());
        return node.getValue().getValue(TypeToken.of(nodeType));
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable DropCondition obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
    }

}
