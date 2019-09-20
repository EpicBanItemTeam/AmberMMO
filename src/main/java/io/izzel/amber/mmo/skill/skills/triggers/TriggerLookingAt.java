package io.izzel.amber.mmo.skill.skills.triggers;

import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillService;
import io.izzel.amber.mmo.skill.skills.Scp173;
import io.izzel.amber.mmo.skill.skills.operations.OperationTargeted;
import io.izzel.amber.mmo.skill.trigger.AbstractTrigger;
import io.izzel.amber.mmo.skill.trigger.util.Function3;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.extent.EntityUniverse;

import java.util.*;
import java.util.stream.Collectors;

public class TriggerLookingAt extends AbstractTrigger<MoveEntityEvent.Position, CastingSkill<?>, OperationTargeted<CastingSkill>, Function3<Entity, Collection<Entity>, Boolean, OperationTargeted<CastingSkill>>> {

    private static final double MAX_DISTANCE = 64;

    private final Map<Entity, Boolean> count = new WeakHashMap<>();
    private final Map<Entity, Set<Entity>> map = new WeakHashMap<>();

    @Override
    public EventListener<MoveEntityEvent.Position> getListener(Function3<Entity, Collection<Entity>, Boolean, OperationTargeted<CastingSkill>> function) {
        return event -> {
            PluginContainer container = event.getContext().get(EventContextKeys.PLUGIN).get();
            Entity entity = event.getTargetEntity();
            count.computeIfAbsent(entity, bool -> {
                Task.builder().delayTicks(5).execute(() -> {
                    Set<Entity> pre = map.computeIfAbsent(entity, it -> new HashSet<>());
                    Set<Entity> cur = entity.getWorld().getIntersectingEntities(event.getToTransform().getPosition(),
                        event.getToTransform().getRotation(), MAX_DISTANCE)
                        .stream().map(EntityUniverse.EntityHit::getEntity).collect(Collectors.toSet());
                    Set<Entity> off = pre.stream().filter(it -> !cur.contains(it)).collect(Collectors.toSet());
                    Set<Entity> on = cur.stream().filter(it -> !pre.contains(it)).collect(Collectors.toSet());
                    SkillService.instance().getOrCreate(entity).operate(Scp173.class, function.apply(entity, off, false));
                    SkillService.instance().getOrCreate(entity).operate(Scp173.class, function.apply(entity, on, true));
                    map.put(entity, cur);
                    count.remove(entity);
                }).submit(container);
                return true;
            });
        };
    }

}
