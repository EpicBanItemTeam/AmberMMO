package io.izzel.amber.mmo.drops.types;

import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.Cause;
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
    public void accepts(Cause cause) {
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            frame.pushCause(this);
            Cause newCause = frame.getCurrentCause();
            dropTable.get(DropTable.RANDOM).forEach(it -> it.accepts(newCause));
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return dropTable.toString();
    }
}
