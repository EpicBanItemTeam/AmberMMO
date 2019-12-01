package io.izzel.amber.mmo.drops.types;

import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.weighted.WeightedObject;
import org.spongepowered.api.util.weighted.WeightedTable;

import java.util.List;

public class WeightedDropTable implements DropTable {

    private final WeightedTable<DropTable> dropTable;

    WeightedDropTable(List<WeightedObject<DropTable>> list, int rolls) {
        this.dropTable = new WeightedTable<>(rolls);
        this.dropTable.addAll(list);
    }

    @Override
    public void accepts(Cause cause) {
        dropTable.get(DropTable.RANDOM).forEach(it -> it.accepts(cause));
    }

    @Override
    public String toString() {
        return dropTable.toString();
    }
}
