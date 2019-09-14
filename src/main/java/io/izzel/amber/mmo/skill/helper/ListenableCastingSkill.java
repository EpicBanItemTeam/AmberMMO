package io.izzel.amber.mmo.skill.helper;

import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.skill.SkillSubject;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.op.SkillOperation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ListenableCastingSkill<E extends EntitySkill<?, ?>> extends ReflectiveCastingSkill<E> {

    private final Map<Class, BiFunction<SkillOperation, SkillSubject.CastOperator, Void>> map = new HashMap<>();

    public ListenableCastingSkill() {
        Type superclass = this.getClass().getGenericSuperclass();
        Preconditions.checkState(superclass instanceof ParameterizedType);
        Type argument = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        Preconditions.checkState(argument instanceof Class);
        for (Map.Entry<Class, Method> entry : Capture.captureMethods(((Class) argument)).entrySet()) {
            map.put(entry.getKey(), (o, c) -> {
                try {
                    entry.getValue().invoke(this, o, c);
                    return null;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void perform(SkillOperation operation, SkillSubject.CastOperator operator) throws UnsupportedOperationException {
        Class<? extends SkillOperation> operationClass = operation.getClass();
        for (Map.Entry<Class, BiFunction<SkillOperation, SkillSubject.CastOperator, Void>> entry : map.entrySet()) {
            if (entry.getKey().isAssignableFrom(operationClass)) {
                entry.getValue().apply(operation, operator);
            }
        }
    }
}
