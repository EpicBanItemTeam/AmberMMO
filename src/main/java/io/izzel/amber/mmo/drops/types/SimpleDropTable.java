package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.event.cause.Cause;

import java.util.List;

public class SimpleDropTable implements DropTable {

    private final String id;
    private final List<DropTable> list;
    private final int roll;

    SimpleDropTable(String id, List<DropTable> list, int roll) {
        this.id = id;
        this.list = list;
        this.roll = roll;
    }

    @Override
    public void accepts(Cause cause) {
        Cause newCause = cause.with(this);
        for (int i = 0; i < roll; i++) {
            for (DropTable table : list) {
                table.accepts(newCause);
            }
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("list", list)
            .toString();
    }
}
