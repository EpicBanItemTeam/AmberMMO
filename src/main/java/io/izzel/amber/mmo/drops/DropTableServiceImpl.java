package io.izzel.amber.mmo.drops;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.drops.types.DropRule;
import io.izzel.amber.mmo.drops.types.DropRuleTypeSerializer;
import io.izzel.amber.mmo.drops.types.conditions.DropCondition;
import io.izzel.amber.mmo.drops.types.conditions.DropConditionTypeSerializer;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.tables.DropTableTypeSerializer;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import io.izzel.amber.mmo.drops.types.tables.amounts.AmountSerializer;
import io.izzel.amber.mmo.drops.types.tables.internal.DropTableEntry;
import io.izzel.amber.mmo.drops.types.triggers.DropTrigger;
import io.izzel.amber.mmo.drops.types.triggers.DropTriggerTypeSerializer;
import lombok.val;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"unchecked", "DuplicatedCode"})
@Singleton
class DropTableServiceImpl implements DropTableService {

    private final Path droptableFolder, rulesFolder;
    private final Map<String, Class<?>> dropTableTypes = new HashMap<>();
    private final Map<String, Class<?>> dropTriggerTypes = new HashMap<>();
    private final Map<String, Class<?>> dropConditionTypes = new HashMap<>();

    private Map<String, Map<String, DropTable>> fileTables = new HashMap<>();
    private Map<String, DropTable> tables = new HashMap<>();
    private Map<String, Map<String, DropRule>> fileRules = new HashMap<>();
    private Map<String, DropRule> rules = new HashMap<>();

    @Inject
    public DropTableServiceImpl(PluginContainer container, Game game, @ConfigDir(sharedRoot = false) Path path) {
        this.droptableFolder = path.resolve("drop_tables");
        this.rulesFolder = path.resolve("drop_rules");
        game.getServiceManager().setProvider(container, DropTableService.class, this);
        TypeSerializers.getDefaultSerializers()
            .registerType(TypeToken.of(DropTable.class), new DropTableTypeSerializer())
            .registerType(TypeToken.of(Amount.class), new AmountSerializer())
            .registerType(TypeToken.of(DropTableEntry.class), new DropTableEntry.Serializer())
            .registerType(TypeToken.of(DropRule.class), new DropRuleTypeSerializer())
            .registerType(TypeToken.of(DropTrigger.class), new DropTriggerTypeSerializer())
            .registerType(TypeToken.of(DropCondition.class), new DropConditionTypeSerializer());
        game.getEventManager().registerListener(container, GamePostInitializationEvent.class, event -> {
            try (CauseStackManager.StackFrame frame = game.getCauseStackManager().pushCauseFrame()) {
                game.getEventManager().post(new RegistryImpl(frame.getCurrentCause()));
            }
            reloadDrops();
        });
        game.getEventManager().registerListener(container, GameStartedServerEvent.class, event -> {
            for (DropRule rule : this.rules.values()) {
                for (DropTrigger trigger : rule.getTriggers()) {
                    trigger.set(rule);
                }
            }
        });
    }

    @Override
    public <T extends DropTable> Class<T> getDropTableTypeById(String id) {
        return (Class<T>) dropTableTypes.get(id);
    }

    @Override
    public <T extends DropTrigger> Class<T> getDropTriggerTypeById(String id) {
        return (Class<T>) dropTriggerTypes.get(id);
    }

    @Override
    public <T extends DropCondition> Class<T> getDropConditionTypeById(String id) {
        return (Class<T>) dropConditionTypes.get(id);
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
    public Optional<DropRule> getDropRuleById(String id) {
        String[] split = id.split("\\.");
        if (split.length > 1) {
            return Optional.ofNullable(
                Optional.ofNullable(fileRules.get(split[0]))
                    .map(it -> it.get(split[1]))
                    .orElseGet(() -> rules.get(split[0]))
            );
        } else {
            return Optional.ofNullable(rules.get(id));
        }
    }

    @Override
    public void reloadDrops() throws Exception {
        for (DropRule rule : this.rules.values()) {
            rule.getTriggers().forEach(DropTrigger::unset);
        }
        this.tables.clear();
        this.fileTables.clear();
        this.rules.clear();
        this.fileRules.clear();
        if (!Files.exists(droptableFolder)) Files.createDirectories(droptableFolder);
        if (!Files.exists(rulesFolder)) Files.createDirectories(rulesFolder);
        val tableItr = Files.walk(droptableFolder).iterator();
        while (tableItr.hasNext()) {
            Path path = tableItr.next();
            if (!Files.isDirectory(path)) {
                Map<String, DropTable> load = load(path, TypeToken.of(DropTable.class));
                this.tables.putAll(load);
                this.fileTables.put(path.getFileName().toString().replaceFirst("[.][^.]+$", ""), load);
            }
        }
        val ruleItr = Files.walk(rulesFolder).iterator();
        while (ruleItr.hasNext()) {
            Path path = ruleItr.next();
            if (!Files.isDirectory(path)) {
                Map<String, DropRule> load = load(path, TypeToken.of(DropRule.class));
                this.rules.putAll(load);
                this.fileRules.put(path.getFileName().toString().replaceFirst("[.][^.]+$", ""), load);
            }
        }
    }

    private <T> Map<String, T> load(Path path, TypeToken<T> token) throws Exception {
        Map<String, T> map = Maps.newHashMap();
        CommentedConfigurationNode node = HoconConfigurationLoader.builder().setPath(path).build().load();
        for (val entry : node.getChildrenMap().entrySet()) {
            try {
                T instance = entry.getValue().getValue(token);
                map.put(entry.getKey().toString(), instance);
            } catch (Throwable ignored) {
            }
        }
        return map;
    }

    @NonnullByDefault
    private final class RegistryImpl implements Registry {

        private final Cause cause;

        private RegistryImpl(Cause cause) {
            this.cause = cause;
        }

        @Override
        public <T extends DropTable> void registerDropTableType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
            dropTableTypes.put(id, cl);
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(cl), deserializer);
        }

        @Override
        public <T extends DropTrigger> void registerDropTriggerType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
            dropTriggerTypes.put(id, cl);
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(cl), deserializer);
        }

        @Override
        public <T extends DropCondition> void registerDropConditionType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
            dropConditionTypes.put(id, cl);
            TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(cl), deserializer);
        }

        @Override
        public Cause getCause() {
            return cause;
        }
    }

}
