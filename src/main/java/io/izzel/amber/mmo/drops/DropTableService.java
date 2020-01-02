package io.izzel.amber.mmo.drops;

import com.google.inject.ImplementedBy;
import io.izzel.amber.mmo.drops.data.AmountTempModifier;
import io.izzel.amber.mmo.drops.processor.DropItemProcessor;
import io.izzel.amber.mmo.drops.types.DropRule;
import io.izzel.amber.mmo.drops.types.conditions.DropCondition;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.triggers.DropTrigger;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Event;

import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

@ImplementedBy(DropTableServiceImpl.class)
public interface DropTableService {

    <T extends DropTable> Class<T> getDropTableTypeById(String id);

    <T extends DropTrigger> Class<T> getDropTriggerTypeById(String id);

    <T extends DropCondition> Class<T> getDropConditionTypeById(String id);

    Optional<DropTable> getDropTableById(String id);

    Optional<DropRule> getDropRuleById(String id);

    void reloadDrops(CommandSource source) throws Exception;

    void addModifier(Entity entity, String amount, AmountTempModifier modifier);

    DoubleUnaryOperator getModifier(Entity entity, String amount);

    DropItemProcessor getDropItemProcessor();

    static DropTableService instance() {
        return Sponge.getServiceManager().provideUnchecked(DropTableService.class);
    }

    interface Registry extends Event {

        <T extends DropTable> void registerDropTableType(String id, Class<T> cl, TypeSerializer<T> deserializer);

        <T extends DropTrigger> void registerDropTriggerType(String id, Class<T> cl, TypeSerializer<T> deserializer);

        <T extends DropCondition> void registerDropConditionType(String id, Class<T> cl, TypeSerializer<T> deserializer);

    }

}
