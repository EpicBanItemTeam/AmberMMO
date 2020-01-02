package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.data.AmountTempModifier;
import io.izzel.amber.mmo.drops.data.DropPlayerData;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.util.DurationUtil;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface DynamicEntry extends DropTable {

    class Serializer implements TypeSerializer<DynamicEntry> {

        @Nullable
        @Override
        public DynamicEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            val node = value.getChildrenMap().entrySet().iterator().next();
            val nodeType = node.getKey().toString();
            val key = node.getValue().getNode("key").getString();
            if (key == null) throw new ObjectMappingException();
            val duration = node.getValue().getNode("duration").getString();
            val expression = node.getValue().getNode("expression").getString();
            if (nodeType.equalsIgnoreCase("add")) {
                if (duration == null || expression == null) throw new ObjectMappingException();
                return new Add(key, duration, expression);
            } else if (nodeType.equalsIgnoreCase("set")) {
                if (duration == null || expression == null) throw new ObjectMappingException();
                return new Set(key, duration, expression);
            } else if (nodeType.equalsIgnoreCase("reset")) {
                return new Reset(key);
            } else {
                throw new ObjectMappingException();
            }
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable DynamicEntry obj, @NonNull ConfigurationNode value) {
        }

        private static class Reset implements DynamicEntry {

            private final String key;

            private Reset(String key) {
                this.key = key;
            }

            @Override
            public void accepts() {
                DropContext.current().get(DropContext.Key.OWNER).ifPresent(entity -> {
                    DropPlayerData.resetModifier(entity, key);
                });
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("key", key)
                    .toString();
            }
        }

        private static class Set implements DynamicEntry {

            private final String key;
            private final long duration;
            private final String expression;

            private Set(String key, String duration, String expression) throws ObjectMappingException {
                this.key = key;
                try {
                    this.duration = DurationUtil.parseToMillis(duration);
                } catch (Exception e) {
                    throw new ObjectMappingException();
                }
                this.expression = expression;
            }

            @Override
            public void accepts() {
                DropContext.current().get(DropContext.Key.OWNER).ifPresent(entity -> {
                    DropPlayerData.setModifier(entity, key,
                        new AmountTempModifier(System.currentTimeMillis() + duration, expression));
                });
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("key", key)
                    .add("duration", duration)
                    .add("expression", expression)
                    .toString();
            }
        }

        private static class Add implements DynamicEntry {

            private final String key;
            private final long duration;
            private final String expression;

            private Add(String key, String duration, String expression) throws ObjectMappingException {
                this.key = key;
                try {
                    this.duration = DurationUtil.parseToMillis(duration);
                } catch (Exception e) {
                    throw new ObjectMappingException();
                }
                this.expression = expression;
            }

            @Override
            public void accepts() {
                DropContext.current().get(DropContext.Key.OWNER).ifPresent(entity -> {
                    DropPlayerData.addModifier(entity, key,
                        new AmountTempModifier(System.currentTimeMillis() + duration, expression));
                });
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                    .add("key", key)
                    .add("duration", duration)
                    .add("expression", expression)
                    .toString();
            }
        }
    }
}
