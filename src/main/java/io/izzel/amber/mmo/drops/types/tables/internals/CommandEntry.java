package io.izzel.amber.mmo.drops.types.tables.internals;

import com.google.common.reflect.TypeToken;
import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;

public class CommandEntry implements DropTable {

    private final String command;

    public CommandEntry(String command) {
        this.command = command;
    }

    @Override
    public void accepts() {
        DropContext context = DropContext.current();
        CommandSource source = context.get(DropContext.Key.OWNER)
            .filter(CommandSource.class::isInstance)
            .map(CommandSource.class::cast)
            .orElse(Sponge.getServer().getConsole());
        Sponge.getCommandManager().process(source, command);
    }

    public static class Serializer implements TypeSerializer<CommandEntry> {

        @Nullable
        @Override
        public CommandEntry deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
            String string = value.getString();
            if (string == null) throw new ObjectMappingException();
            return new CommandEntry(string);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, @Nullable CommandEntry obj, @NonNull ConfigurationNode value) {
        }
    }
}
