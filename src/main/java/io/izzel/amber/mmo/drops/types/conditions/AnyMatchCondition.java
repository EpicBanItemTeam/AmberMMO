package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class AnyMatchCondition implements CooldownCondition {

    private final List<DropCondition> conditions;

    public AnyMatchCondition(List<DropCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test() {
        return conditions.stream().anyMatch(DropCondition::test);
    }

    public static class Serializer implements TypeSerializer<AnyMatchCondition> {

        @Nullable
        @Override
        public AnyMatchCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            return new AnyMatchCondition(value.getList(TypeToken.of(DropCondition.class)));
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable AnyMatchCondition obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

        }
    }
}
