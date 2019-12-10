package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.drops.DropTable;
import org.spongepowered.api.Sponge;

public class ChanceTableEntry implements DropTable {

    private final String id;

    private final DropTable actual;

    private final double probability;

    private final Amount amount;

    ChanceTableEntry(String id, DropTable actual, double probability, Amount amount) {
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
    public void accepts() {
        if (DropTable.RANDOM.nextDouble() < probability) {
            Sponge.getCauseStackManager().pushCause(this);
            int count = (int) amount.get();
            for (int i = 0; i < count; i++) {
                actual.accepts();
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
            .add("id", id)
            .add("actual", actual)
            .add("probability", probability)
            .add("amount", amount)
            .toString();
    }
}
