package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.Cause;

public class SimpleDropTableEntry implements DropTable {

    private final String id;

    private final DropTable actual;

    private final double probability;

    private final Amount amount;

    SimpleDropTableEntry(String id, DropTable actual, double probability, Amount amount) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(actual);
        Preconditions.checkArgument(probability >= 0 && probability <= 1, "probability should be in [0, 1]");
        Preconditions.checkNotNull(amount);
        this.id = id;
        this.actual = actual;
        this.probability = probability;
        this.amount = amount;
    }

    @Override
    public void accepts(Cause cause) {
        if (DropTable.RANDOM.nextDouble() < probability) {
            try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                frame.pushCause(this);
                Cause newCause = frame.getCurrentCause();
                int count = (int) amount.get(newCause);
                for (int i = 0; i < count; i++) {
                    actual.accepts(newCause);
                }
            }
        }
    }

    @Override
    public String getId() {
        return id;
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
