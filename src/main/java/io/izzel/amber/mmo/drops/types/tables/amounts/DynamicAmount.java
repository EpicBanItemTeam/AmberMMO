package io.izzel.amber.mmo.drops.types.tables.amounts;

import com.google.common.base.MoreObjects;
import io.izzel.amber.mmo.util.Identified;

public class DynamicAmount implements Amount, Identified {

    private final String id;
    private final Amount base;

    public DynamicAmount(String id, Amount base) {
        this.id = id;
        this.base = base;
    }

    @Override
    public double get() {
        double d = base.get();
        //todo
        return d;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("base", base)
            .toString();
    }

    @Override
    public String getId() {
        return id;
    }

}
