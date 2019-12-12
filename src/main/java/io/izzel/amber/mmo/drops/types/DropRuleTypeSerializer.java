package io.izzel.amber.mmo.drops.types;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.types.conditions.DropCondition;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.triggers.DropTrigger;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class DropRuleTypeSerializer implements TypeSerializer<DropRule> {

    @Nullable
    @Override
    public DropRule deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        List<DropTrigger> triggers = value.getNode("triggers").getList(TypeToken.of(DropTrigger.class));
        List<DropCondition> conditions = value.getNode("conditions").getList(TypeToken.of(DropCondition.class));
        List<DropTable> actions = value.getNode("actions").getList(TypeToken.of(DropTable.class));
        return new SimpleDropRule(triggers, conditions, actions);
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable DropRule obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
    }
}
