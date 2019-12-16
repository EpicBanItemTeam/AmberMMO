package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropTableService;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DropTableEntry implements DropTable {

    private final String table;

    public DropTableEntry(String table) {
        Preconditions.checkNotNull(table);
        this.table = table;
    }

    @Override
    public void accepts() {
        DropTableService.instance().getDropTableById(table).ifPresent(DropTable::accepts);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("table", table)
            .toString();
    }

    public static class Serializer implements TypeSerializer<DropTableEntry> {

        @Nullable
        @Override
        public DropTableEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            String id = value.getNode("id").getString();
            if (id == null) throw new ObjectMappingException();
            else return new DropTableEntry(id);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable DropTableEntry obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
        }
    }
}
