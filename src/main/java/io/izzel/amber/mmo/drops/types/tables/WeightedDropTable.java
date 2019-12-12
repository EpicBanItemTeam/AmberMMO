package io.izzel.amber.mmo.drops.types.tables;

import com.google.common.base.MoreObjects;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.weighted.WeightedObject;
import org.spongepowered.api.util.weighted.WeightedTable;

import java.util.List;

public class WeightedDropTable implements DropTable {

    private final String id;

    private final WeightedTable<DropTable> dropTable;

    WeightedDropTable(String id, List<WeightedObject<DropTable>> list, int rolls) {
        this.id = id;
        this.dropTable = new WeightedTable<>(rolls);
        this.dropTable.addAll(list);
    }

    @Override
    public void accepts() {
        Sponge.getCauseStackManager().pushCause(this);
        dropTable.get(DropTable.RANDOM).forEach(DropTable::accepts);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("table", dropTable)
            .toString();
    }
}
