package io.izzel.amber.mmo.skill.trigger;

import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.trigger.util.OperateFunction;
import org.spongepowered.api.event.Event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractTrigger<E extends Event, C extends CastingSkill, O extends SkillOperation<? super C>, F extends OperateFunction<O>>
    implements Trigger<E, C, O, F> {

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
