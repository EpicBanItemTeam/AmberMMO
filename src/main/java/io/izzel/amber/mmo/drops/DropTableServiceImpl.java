package io.izzel.amber.mmo.drops;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.drops.types.DropRule;
import io.izzel.amber.mmo.drops.types.DropRuleTypeSerializer;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.tables.DropTableTypeSerializer;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import io.izzel.amber.mmo.drops.types.tables.amounts.AmountSerializer;
import io.izzel.amber.mmo.drops.types.tables.internal.DropTableEntry;
import lombok.val;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
class DropTableServiceImpl implements DropTableService {

    private final Path droptableFolder;
    private final Map<String, Class<?>> map = new HashMap<>();
    private Map<String, Map<String, DropTable>> fileTables = new HashMap<>();
    private Map<String, DropTable> tables = new HashMap<>();

    @Inject
    public DropTableServiceImpl(PluginContainer container, Game game, @ConfigDir(sharedRoot = false) Path path) {
        this.droptableFolder = path.resolve("droptables");
        game.getServiceManager().setProvider(container, DropTableService.class, this);
        TypeSerializers.getDefaultSerializers()
            .registerType(TypeToken.of(DropTable.class), new DropTableTypeSerializer())
            .registerType(TypeToken.of(Amount.class), new AmountSerializer())
            .registerType(TypeToken.of(DropTableEntry.class), new DropTableEntry.Serializer())
            .registerType(TypeToken.of(DropRule.class), new DropRuleTypeSerializer());
        game.getEventManager().registerListener(container, GamePostInitializationEvent.class, event -> reloadDropTables());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DropTable> Class<T> getTypeById(String id) {
        return (Class<T>) map.get(id);
    }

    @Override
    public Optional<DropTable> getDropTableById(String id) {
        String[] split = id.split("\\.");
        if (split.length > 1) {
            return Optional.ofNullable(
                Optional.ofNullable(fileTables.get(split[0]))
                    .map(it -> it.get(split[1]))
                    .orElseGet(() -> tables.get(split[0]))
            );
        } else {
            return Optional.ofNullable(tables.get(id));
        }
    }

    @Override
    public void reloadDropTables() throws Exception {
        this.tables.clear();
        this.fileTables.clear();
        if (!Files.exists(droptableFolder)) Files.createDirectories(droptableFolder);
        val iterator = Files.walk(droptableFolder).iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            if (!Files.isDirectory(path)) {
                Map<String, DropTable> load = load(path);
                this.tables.putAll(load);
                this.fileTables.put(path.getFileName().toString().replaceFirst("[.][^.]+$", ""), load);
            }
        }
    }

    private Map<String, DropTable> load(Path path) throws Exception {
        Map<String, DropTable> map = Maps.newHashMap();
        CommentedConfigurationNode node = HoconConfigurationLoader.builder().setPath(path).build().load();
        for (val entry : node.getChildrenMap().entrySet()) {
            try {
                DropTable dropTable = entry.getValue().getValue(TypeToken.of(DropTable.class));
                map.put(entry.getKey().toString(), dropTable);
            } catch (Throwable ignored) {
            }
        }
        return map;
    }

}
