package io.izzel.amber.mmo.skill.skills.triggers;

import io.izzel.amber.mmo.skill.trigger.Trigger;
import io.izzel.amber.mmo.skill.trigger.dispatcher.Dispatcher3;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.entity.MoveEntityEvent;

import java.util.Collection;

public final class Triggers {

    public static Trigger<MoveEntityEvent.Position, Dispatcher3<Entity, Collection<Entity>, Collection<Entity>>> lookingAt() {
        return new TriggerLookingAt();
    }
}
