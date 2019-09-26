package io.izzel.amber.mmo.skill.trigger;

import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.skill.trigger.dispatcher.OperateDispatcher;
import org.spongepowered.api.event.Event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractTrigger<E extends Event, F extends OperateDispatcher>
    implements Trigger<E, F> {

    private final Class<E> cl;

    @SuppressWarnings("unchecked")
    public AbstractTrigger() {
        Type superclass = this.getClass().getGenericSuperclass();
        Preconditions.checkState(superclass instanceof ParameterizedType);
        Type argument = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        Preconditions.checkState(argument instanceof Class);
        Preconditions.checkState(Event.class.isAssignableFrom(((Class) argument)));
        cl = ((Class<E>) argument);
    }

    @Override
    public Class<E> getEventClass() {
        return cl;
    }
}
