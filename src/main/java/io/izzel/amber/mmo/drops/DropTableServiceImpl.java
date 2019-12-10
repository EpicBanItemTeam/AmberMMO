package io.izzel.amber.mmo.drops;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.drops.types.Amount;
import io.izzel.amber.mmo.drops.types.AmountSerializer;
import io.izzel.amber.mmo.drops.types.DropTableTypeSerializer;
import io.izzel.amber.mmo.drops.types.internal.DropTableEntry;
import lombok.val;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;

import java.nio.file.Path;
import java.util.*;

@Singleton
class DropTableServiceImpl implements DropTableService {

    private final Path path;
    private final Map<String, Class<?>> map = new HashMap<>();
    private Map<String, DropTable> tables = new HashMap<>();

    @Inject
    public DropTableServiceImpl(PluginContainer container, Game game, @ConfigDir(sharedRoot = false) Path path) {
        this.path = path.resolve("droptable.conf");
        game.getServiceManager().setProvider(container, DropTableService.class, this);
        TypeSerializers.getDefaultSerializers()
            .registerType(TypeToken.of(DropTable.class), new DropTableTypeSerializer())
            .registerType(TypeToken.of(Amount.class), new AmountSerializer())
            .registerType(TypeToken.of(DropTableEntry.class), new DropTableEntry.Serializer());
        game.getEventManager().registerListener(container, GamePostInitializationEvent.class, event -> reloadDropTables());
    }

    @Override
    public <T extends DropTable> void registerDropTableType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
        Preconditions.checkArgument(!map.containsKey(id), "duplicate id");
        Preconditions.checkNotNull(cl);
        Preconditions.checkNotNull(deserializer);
        map.put(id, cl);
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(cl), deserializer);
    }

    @Override
    public Set<String> availableTypes() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DropTable> Class<T> getTypeById(String id) {
        return (Class<T>) map.get(id);
    }

    @Override
    public Optional<DropTable> getDropTableById(String id) {
        return Optional.ofNullable(tables.get(id));
    }

    @Override
    public void reloadDropTables() throws Exception {
        Map<String, DropTable> map = Maps.newHashMap();
        CommentedConfigurationNode node = HoconConfigurationLoader.builder().setPath(path).build().load();
        for (val entry : node.getChildrenMap().entrySet()) {
            try {
                DropTable dropTable = entry.getValue().getValue(TypeToken.of(DropTable.class));
                map.put(entry.getKey().toString(), dropTable);
            } catch (Throwable ignored) {
            }
        }
        this.tables.clear();
        this.tables.putAll(map);
    }

}
