package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class NotCondition implements DropCondition {

    private final DropCondition condition;

    public NotCondition(DropCondition condition) {
        this.condition = condition;
    }

    @Override
    public boolean test() {
        return !condition.test();
    }

    @Override
    public String toString() {
        return "!" + condition.toString();
    }

    public static class Serializer implements TypeSerializer<NotCondition> {

        @Nullable
        @Override
        public NotCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            return new NotCondition(value.getValue(TypeToken.of(DropCondition.class)));
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable NotCondition obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
        }
    }
}
