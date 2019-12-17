package io.izzel.amber.mmo.drops.types.conditions;

import com.google.common.reflect.TypeToken;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.util.Coerce;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface DateCondition extends DropCondition {

    class Serializer implements TypeSerializer<DateCondition> {

        @Nullable
        @Override
        public DateCondition deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            val node = value.getChildrenMap().entrySet().iterator().next();
            val nodeType = node.getKey().toString();
            if (nodeType.equalsIgnoreCase("between")) {
                String from = node.getValue().getNode("from").getString("");
                String to = node.getValue().getNode("to").getString("");
                try {
                    LocalTime fromTime = LocalTime.parse(from);
                    LocalTime toTime = LocalTime.parse(to);
                    return () -> {
                        LocalTime now = LocalTime.now();
                        return !now.isBefore(fromTime) && now.isBefore(toTime);
                    };
                } catch (Exception ignored) {
                }
                try {
                    LocalDate fromDate = LocalDate.parse(from);
                    LocalDate toDate = LocalDate.parse(to);
                    return () -> {
                        LocalDate now = LocalDate.now();
                        return !now.isBefore(fromDate) && now.isBefore(toDate);
                    };
                } catch (Exception ignored) {
                }
                try {
                    LocalDateTime fromDateTime = LocalDateTime.parse(from);
                    LocalDateTime toDateTime = LocalDateTime.parse(to);
                    return () -> {
                        LocalDateTime now = LocalDateTime.now();
                        return !now.isBefore(fromDateTime) && now.isBefore(toDateTime);
                    };
                } catch (Exception ignored) {
                }
            } else if (nodeType.equalsIgnoreCase("in-week")) {
                List<Integer> list = node.getValue().getList(Coerce::toInteger);
                return () -> {
                    LocalDateTime now = LocalDateTime.now();
                    return list.contains(now.getDayOfWeek().getValue());
                };
            }
            return null;
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable DateCondition obj, @NonNull ConfigurationNode value) {
        }
    }

}
