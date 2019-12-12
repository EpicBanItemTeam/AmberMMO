package io.izzel.amber.mmo.drops.types.tables;

import com.google.common.base.MoreObjects;

import java.util.List;

public class NestedDropTable implements DropTable {

    private final List<DropTable> tables;

    public NestedDropTable(List<DropTable> tables) {
        this.tables = tables;
    }

    @Override
    public void accepts() {
        tables.forEach(DropTable::accepts);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("tables", tables)
            .toString();
    }
}
