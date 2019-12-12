package io.izzel.amber.mmo.drops.types.tables.amounts;

public interface Amount {

    double get();

    static Amount fixed(double d) {
        return AmountSerializer.fixed(d);
    }

    static Amount ranged(double l, double r) {
        return AmountSerializer.ranged(l, r);
    }

}
