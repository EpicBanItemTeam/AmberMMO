package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class OverrideEntry implements DropTable {

    @Override
    public void accepts() {
        DropContext.current().setOverrideDefault();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .toString();
    }

    public static class Serializer implements TypeSerializer<OverrideEntry> {

        @Nullable
        @Override
        public OverrideEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {
            return new OverrideEntry();
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable OverrideEntry obj, @NonNull ConfigurationNode value) {
        }
    }
}
