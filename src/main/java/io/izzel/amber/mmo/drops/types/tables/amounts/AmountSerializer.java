package io.izzel.amber.mmo.drops.types.tables.amounts;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.util.Coerce;

import java.util.List;

public class AmountSerializer implements TypeSerializer<Amount> {

    @Nullable
    @Override
    public Amount deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        switch (value.getValueType()) {
            case SCALAR:
                return Amount.fixed(Coerce.toDouble(value.getValue()));
            case MAP: break;
            case LIST:
                List<Double> list = value.getList(Coerce::toDouble);
                if (list.size() == 1) {
                    double i = list.get(0);
                    return Amount.fixed(i);
                } else if (list.size() == 2) {
                    double l = list.get(0), r = list.get(1);
                    return Amount.ranged(l, r);
                }
                break;
            case NULL: return Amount.fixed(1);
        }
        throw new ObjectMappingException("Unknown amount type: " + value);
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Amount obj, @NonNull ConfigurationNode value) {
    }

    static Amount fixed(double d) {
        return new Fixed(d);
    }

    static Amount ranged(double l, double r) {
        return new Ranged(l, r);
    }

    private static class Fixed implements Amount {

        private final double i;

        private Fixed(double i) {
            this.i = i;
        }

        @Override
        public double get() {
            return i;
        }

        @Override
        public String toString() {
            return String.valueOf(i);
        }
    }

    private static class Ranged implements Amount {

        private final double l, r;

        private Ranged(double l, double r) {
            this.l = Math.min(l, r);
            this.r = Math.max(l, r);
        }

        @Override
        public double get() {
            return DropTable.RANDOM.nextDouble() * (r - l) + l;
        }

        @Override
        public String toString() {
            return String.format("[%s, %s)", l, r);
        }
    }
}
