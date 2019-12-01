package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.event.cause.Cause;

import java.util.List;

public class SimpleDropTable implements DropTable {

    private final List<DropTable> list;

    SimpleDropTable(List<DropTable> list) {
        this.list = list;
    }

    @Override
    public void accepts(Cause cause) {
        for (DropTable table : list) {
            table.accepts(cause);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("list", list)
            .toString();
    }
}
