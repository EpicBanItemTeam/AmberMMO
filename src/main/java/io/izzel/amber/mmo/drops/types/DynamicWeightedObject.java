package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.weighted.WeightedObject;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

@NonnullByDefault
public class DynamicWeightedObject<T> extends WeightedObject<T> implements DropTable {

    private final String id;
    private final Amount amount;

    public DynamicWeightedObject(String id, T obj, Amount amount) {
        super(obj, 0);
        this.id = id;
        this.amount = amount;
    }

    @Override
    public double getWeight() {
        return amount.get();
    }

    @Override
    public void accepts() {
    }

    @Override
    public T get() {
        Cause cause = Sponge.getCauseStackManager().getCurrentCause();
        if (!cause.contains(this)) {
            Sponge.getCauseStackManager().pushCause(this);
        }
        return super.get();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("amount", amount)
            .add("table", super.get())
            .toString();
    }
}
