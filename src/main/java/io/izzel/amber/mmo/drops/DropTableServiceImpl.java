package io.izzel.amber.mmo.drops;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.izzel.amber.mmo.drops.data.AmountTempModifier;
import io.izzel.amber.mmo.drops.data.DropPlayerData;
import io.izzel.amber.mmo.drops.processor.DropItemProcessor;
import io.izzel.amber.mmo.drops.processor.SimpleDropItemProcessor;
import io.izzel.amber.mmo.drops.types.DropRule;
import io.izzel.amber.mmo.drops.types.DropRuleTypeSerializer;
import io.izzel.amber.mmo.drops.types.conditions.*;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.tables.DropTableTypeSerializer;
import io.izzel.amber.mmo.drops.types.tables.amounts.Amount;
import io.izzel.amber.mmo.drops.types.tables.amounts.AmountSerializer;
import io.izzel.amber.mmo.drops.types.tables.internals.CommandEntry;
import io.izzel.amber.mmo.drops.types.tables.internals.DropTableEntry;
import io.izzel.amber.mmo.drops.types.tables.internals.ExpEntry;
import io.izzel.amber.mmo.drops.types.tables.internals.VanillaEntry;
import io.izzel.amber.mmo.drops.types.triggers.*;
import lombok.val;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.plugin.meta.util.NonnullByDefault;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

@SuppressWarnings({"unchecked", "DuplicatedCode"})
@Singleton
class DropTableServiceImpl implements DropTableService {

    private final Path droptableFolder, rulesFolder;
    private final Map<String, Class<?>> dropTableTypes = new HashMap<>();
    private final Map<String, Class<?>> dropTriggerTypes = new HashMap<>();
    private final Map<String, Class<?>> dropConditionTypes = new HashMap<>();
    private final TypeSerializerCollection internalCol = TypeSerializers.getDefaultSerializers().newChild();
    private final DropItemProcessor processor;

    private Map<String, DropTable> tables = new HashMap<>();
    private Map<String, DropRule> rules = new HashMap<>();

    @Inject
    public DropTableServiceImpl(PluginContainer container, Game game, @ConfigDir(sharedRoot = false) Path path, Injector injector) {
        this.droptableFolder = path.resolve("drop_tables");
        this.rulesFolder = path.resolve("drop_rules");
        this.processor = injector.getInstance(SimpleDropItemProcessor.class); // todo more implementations
        game.getServiceManager().setProvider(container, DropTableService.class, this);
        TypeSerializers.getDefaultSerializers()
            .registerType(TypeToken.of(DropTable.class), new DropTableTypeSerializer())
            .registerType(TypeToken.of(Amount.class), new AmountSerializer())
            .registerType(TypeToken.of(DropRule.class), new DropRuleTypeSerializer())
            .registerType(TypeToken.of(DropTrigger.class), new DropTriggerTypeSerializer())
            .registerType(TypeToken.of(DropCondition.class), new DropConditionTypeSerializer());
        game.getEventManager().registerListener(container, GameInitializationEvent.class, event -> {
            DataRegistration.builder()
                .dataClass(DropPlayerData.Mutable.class)
                .immutableClass(DropPlayerData.Immutable.class)
                .builder(new DropPlayerData.Builder())
                .id("ambermmo_cd")
                .name("AmberMMO Cooldown Data")
                .build();
            game.getDataManager().registerBuilder(AmountTempModifier.class, new AmountTempModifier.Builder());
        });
        game.getEventManager().registerListener(container, GamePostInitializationEvent.class, event -> {
            try (CauseStackManager.StackFrame frame = game.getCauseStackManager().pushCauseFrame()) {
                game.getEventManager().post(new RegistryImpl(frame.getCurrentCause()));
            }
            reloadDrops();
        });
        game.getEventManager().registerListener(container, GameStartedServerEvent.class, event -> {
            for (DropRule rule : this.rules.values()) {
                for (DropTrigger trigger : rule.getTriggers()) {
                    trigger.set(rule::apply);
                }
            }
        });
        game.getEventManager().registerListener(container, DropTableService.Registry.class, event -> {
            event.registerDropTableType("drop-table", DropTableEntry.class, new DropTableEntry.Serializer());
            event.registerDropTableType("vanilla", VanillaEntry.class, new VanillaEntry.Serializer());
            event.registerDropTableType("exp", ExpEntry.class, new ExpEntry.Serializer());
            event.registerDropTableType("command", CommandEntry.class, new CommandEntry.Serializer());
            event.registerDropConditionType("cooldown", CooldownCondition.class, new CooldownCondition.Serializer());
            event.registerDropConditionType("any", AnyMatchCondition.class, new AnyMatchCondition.Serializer());
            event.registerDropConditionType("not", NotCondition.class, new NotCondition.Serializer());
            event.registerDropConditionType("in-region", InRegionCondition.class, new InRegionCondition.Serializer());
            event.registerDropConditionType("date", DateCondition.class, new DateCondition.Serializer());
            event.registerDropTriggerType("timer", TimerTrigger.class, new TimerTrigger.Serializer());
            event.registerDropTriggerType("block-break", BlockBreakTrigger.class, new BlockBreakTrigger.Serializer());
            event.registerDropTriggerType("entity-kill", EntityKillTrigger.class, new EntityKillTrigger.Serializer());
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
        return Optional.ofNullable(tables.get(id));
    }

    @Override
    public Optional<DropRule> getDropRuleById(String id) {
        return Optional.ofNullable(rules.get(id));
    }

    @Override
    public void reloadDrops() throws Exception {
        for (DropRule rule : this.rules.values()) {
            rule.getTriggers().forEach(DropTrigger::unset);
        }
        this.tables.clear();
        this.rules.clear();
        if (!Files.exists(droptableFolder)) Files.createDirectories(droptableFolder);
        if (!Files.exists(rulesFolder)) Files.createDirectories(rulesFolder);
        val tableItr = Files.walk(droptableFolder).iterator();
        while (tableItr.hasNext()) {
            Path path = tableItr.next();
            if (!Files.isDirectory(path)) {
                Map<String, DropTable> load = load(path, TypeToken.of(DropTable.class));
                this.tables.putAll(load);
            }
        }
        val ruleItr = Files.walk(rulesFolder).iterator();
        while (ruleItr.hasNext()) {
            Path path = ruleItr.next();
            if (!Files.isDirectory(path)) {
                Map<String, DropRule> load = load(path, TypeToken.of(DropRule.class));
                this.rules.putAll(load);
            }
        }
    }

    @Override
    public void addModifier(Entity entity, String amount, AmountTempModifier modifier) {
        DropPlayerData.addModifier(entity, amount, modifier);
    }

    @Override
    public DoubleUnaryOperator getModifier(Entity entity, String amount) {
        return DropPlayerData.getModifiers(entity, amount);
    }

    @Override
    public DropItemProcessor getDropItemProcessor() {
        return processor;
    }

    private <T> Map<String, T> load(Path path, TypeToken<T> token) throws Exception {
        Map<String, T> map = Maps.newHashMap();
        ConfigurationOptions options = ConfigurationOptions.defaults().setSerializers(internalCol);
        CommentedConfigurationNode node = HoconConfigurationLoader.builder().setPath(path).build().load(options);
        for (val entry : node.getChildrenMap().entrySet()) {
            try {
                T instance = entry.getValue().getValue(token);
                map.put(entry.getKey().toString(), instance);
            } catch (Throwable e) {
                e.printStackTrace();
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
            internalCol.registerType(TypeToken.of(cl), deserializer);
        }

        @Override
        public <T extends DropTrigger> void registerDropTriggerType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
            dropTriggerTypes.put(id, cl);
            internalCol.registerType(TypeToken.of(cl), deserializer);
        }

        @Override
        public <T extends DropCondition> void registerDropConditionType(String id, Class<T> cl, TypeSerializer<T> deserializer) {
            dropConditionTypes.put(id, cl);
            internalCol.registerType(TypeToken.of(cl), deserializer);
        }

        @Override
        public Cause getCause() {
            return cause;
        }
    }

}
