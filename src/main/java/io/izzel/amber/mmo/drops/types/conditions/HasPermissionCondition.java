package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Coerce;

import java.util.List;

public class HasPermissionCondition implements DropCondition {

    private final List<String> perms;

    public HasPermissionCondition(List<String> perms) {
        this.perms = perms;
    }

    @Override
    public boolean test() {
        DropContext context = DropContext.current();
        return context.get(DropContext.Key.OWNER)
            .filter(Subject.class::isInstance)
            .map(Subject.class::cast)
            .filter(it -> perms.stream().allMatch(it::hasPermission))
            .isPresent();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("perms", perms)
            .toString();
    }

    public static class Serializer implements TypeSerializer<HasPermissionCondition> {

        @Nullable
        @Override
        public HasPermissionCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
            List<String> list = value.getList(Coerce::toString);
            return new HasPermissionCondition(list);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable HasPermissionCondition obj, @NonNull ConfigurationNode value) {
        }
    }
}
