package io.izzel.amber.mmo.skill.trigger;

import io.izzel.amber.mmo.skill.trigger.dispatcher.OperateDispatcher;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;

public interface Trigger<E extends Event, F extends OperateDispatcher> {

    Class<E> getEventClass();

    EventListener<E> getListener(F dispatcher);

}
