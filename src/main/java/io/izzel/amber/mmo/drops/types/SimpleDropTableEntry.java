package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.event.cause.Cause;

public class SimpleDropTableEntry implements DropTable {

    private final DropTable actual;

    private final double probability;

    private final Amount amount;

    SimpleDropTableEntry(DropTable actual, double probability, Amount amount) {
        Preconditions.checkNotNull(actual);
        Preconditions.checkArgument(probability >= 0 && probability <= 1, "probability should be in [0, 1]");
        Preconditions.checkNotNull(amount);
        this.actual = actual;
        this.probability = probability;
        this.amount = amount;
    }

    @Override
    public void accepts(Cause cause) {
        if (DropTable.RANDOM.nextDouble() < probability) {
            int count = (int) amount.get(cause);
            for (int i = 0; i < count; i++) {
                actual.accepts(cause);
            }
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("actual", actual)
            .add("probability", probability)
            .add("amount", amount)
            .toString();
    }
}
