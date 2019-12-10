package io.izzel.amber.mmo.drops.types;

public interface Amount {

    double get();

    double expectation();

    static Amount fixed(double d) {
        return AmountSerializer.fixed(d);
    }

    static Amount ranged(double l, double r) {
        return AmountSerializer.ranged(l, r);
    }

}
