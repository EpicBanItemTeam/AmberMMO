package io.izzel.amber.mmo.drops.types;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.weighted.WeightedObject;

public class DynamicWeightedObject<T> extends WeightedObject<T> {

    private final Amount amount;

    public DynamicWeightedObject(T obj, Amount amount) {
        super(obj, 0);
        this.amount = amount;
    }

    @Override
    public double getWeight() {
        return amount.get(Sponge.getCauseStackManager().getCurrentCause());
    }
}
