package io.izzel.amber.mmo.drops.data;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.Coerce;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NonnullByDefault
public class AmountTempModifier implements DataSerializable {

    private static final Pattern EXPRESSION = Pattern.compile("([-+*])(-?\\d+(\\.\\d+)?)");

    private final long timeout;
    private final String expression;

    @Nullable private DoubleUnaryOperator operator;

    public AmountTempModifier(long timeout, String expression) {
        this.timeout = timeout;
        this.expression = expression;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, getContentVersion())
            .set(Builder.TIMEOUT, timeout)
            .set(Builder.EXPRESSION, expression);
    }

    public long getTimeout() {
        return timeout;
    }

    public DoubleUnaryOperator getOperator() {
        if (operator == null) {
            Matcher matcher = EXPRESSION.matcher(expression);
            if (matcher.find()) {
                double d = Coerce.toDouble(matcher.group(2));
                String op = matcher.group(1);
                if (op.equals("+")) operator = x -> x + d;
                else if (op.equals("-")) operator = x -> x - d;
                else operator = x -> x * d;
            }
        }
        if (operator == null) operator = DoubleUnaryOperator.identity();
        return operator;
    }

    public static class Builder extends AbstractDataBuilder<AmountTempModifier> {

        private static final DataQuery TIMEOUT = DataQuery.of("Timeout");
        private static final DataQuery EXPRESSION = DataQuery.of("Expression");

        public Builder() {
            super(AmountTempModifier.class, 0);
        }

        @Override
        protected Optional<AmountTempModifier> buildContent(DataView container) throws InvalidDataException {
            long timeout = container.getLong(TIMEOUT).orElseThrow(InvalidDataException::new);
            String exp = container.getString(EXPRESSION).orElseThrow(InvalidDataException::new);
            return Optional.of(new AmountTempModifier(timeout, exp));
        }
    }
}
