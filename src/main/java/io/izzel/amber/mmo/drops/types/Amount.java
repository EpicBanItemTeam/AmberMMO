package io.izzel.amber.mmo.drops.types;

import org.spongepowered.api.event.cause.Cause;

public interface Amount {

    double get(Cause cause);

    double expectation();

}
