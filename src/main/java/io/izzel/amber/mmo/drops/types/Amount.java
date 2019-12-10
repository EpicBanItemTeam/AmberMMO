package io.izzel.amber.mmo.drops.types;

import org.spongepowered.api.event.cause.Cause;

public interface Amount {

    double get(Cause cause);

    double expectation();

    static Amount fixed(double d) {
        return new AmountSerializer.Fixed(d);
    }

    static Amount ranged(double l, double r) {
        return new AmountSerializer.Ranged(l, r);
    }

}
