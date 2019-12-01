package io.izzel.amber.mmo.drops.types;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropTable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.Types;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.Coerce;

import java.util.List;

public class AmountSerializer implements TypeSerializer<Amount> {

    @Nullable
    @Override
    public Amount deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        switch (value.getValueType()) {
            case SCALAR:
                return new Fixed(Coerce.toInteger(value.getValue()));
            case MAP: break;
            case LIST:
                List<Integer> list = value.getList(Types::asInt);
                if (list.size() == 1) {
                    double i = list.get(0);
                    return new Fixed(i);
                } else if (list.size() == 2) {
                    double l = list.get(0), r = list.get(1);
                    return new Ranged(l, r + 1);
                }
                break;
            case NULL: return new Fixed(1);
        }
        throw new ObjectMappingException("Unknown amount type: " + value);
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Amount obj, @NonNull ConfigurationNode value) {
    }

    static class Fixed implements Amount {

        private final double i;

        Fixed(double i) {
            this.i = i;
        }

        @Override
        public double get(Cause cause) {
            return i;
        }

        @Override
        public double expectation() {
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
        public double get(Cause cause) {
            return DropTable.RANDOM.nextDouble() * (r - l) + l;
        }

        @Override
        public double expectation() {
            return (r - l) / 2d + l;
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", l, r);
        }
    }
}
