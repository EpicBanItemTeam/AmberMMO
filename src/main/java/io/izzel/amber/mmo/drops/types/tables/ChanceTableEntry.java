package io.izzel.amber.mmo.drops.types.tables;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import org.spongepowered.api.Sponge;

public class ChanceTableEntry implements DropTable {

    private final String id;

    private final DropTable actual;

    private final Amount probability;

    private final Amount amount;

    ChanceTableEntry(String id, DropTable actual, Amount probability, Amount amount) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(actual);
        Preconditions.checkNotNull(probability);
        Preconditions.checkNotNull(amount);
        this.id = id;
        this.actual = actual;
        this.probability = probability;
        this.amount = amount;
    }

    @Override
    public void accepts() {
        double d = probability.get();
        if (DropTable.RANDOM.nextDouble() < d) {
            Sponge.getCauseStackManager().pushCause(this);
            do {
                int count = (int) amount.get();
                for (int i = 0; i < count; i++) {
                    actual.accepts();
                }
                d -= 1D;
            } while (DropTable.RANDOM.nextDouble() < d);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("actual", actual)
            .add("probability", probability)
            .add("amount", amount)
            .toString();
    }
}
