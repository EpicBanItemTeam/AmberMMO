package io.izzel.amber.mmo.drops;

import org.spongepowered.api.event.cause.Cause;

import java.util.Random;

public interface DropTable {

    Random RANDOM = new Random();

    void accepts(Cause cause);

}
