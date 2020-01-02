package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;

public interface CommandEntry extends DropTable {

    class Serializer implements TypeSerializer<CommandEntry> {

        @Nullable
        @Override
        public CommandEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            if (value.getValueType() == ValueType.MAP) {
                val node = value.getChildrenMap().entrySet().iterator().next();
                if (node.getKey().toString().equalsIgnoreCase("console")) {
                    String string = node.getValue().getString();
                    if (string == null) throw new ObjectMappingException();
                    return new Console(string);
                }
            } else {
                String string = value.getString();
                if (string == null) throw new ObjectMappingException();
                return new Normal(string);
            }
            throw new ObjectMappingException();
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable CommandEntry obj, @NonNull ConfigurationNode value) {
        }

        private static class Normal implements CommandEntry {

            private final String command;

            public Normal(String command) {
                this.command = command;
            }

            @Override
            public void accepts() {
                DropContext.current().get(DropContext.Key.OWNER)
                    .filter(CommandSource.class::isInstance)
                    .map(CommandSource.class::cast)
                    .ifPresent(source -> Sponge.getCommandManager().process(source, command));
            }
        }

        private static class Console implements CommandEntry {

            private final String command;

            public Console(String command) {
                this.command = command;
            }

            @Override
            public void accepts() {
                Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
            }
        }
    }
}
