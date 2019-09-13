package io.izzel.amber.mmo.util;

import io.izzel.amber.mmo.skill.data.EntitySkill;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public final class Reflections {

    public static Optional<Class<?>> resolveCasting(Class<?> entitySkillCl) {
        for (Type type : entitySkillCl.getGenericInterfaces()) {
            if (type.equals(EntitySkill.class) && type instanceof ParameterizedType) {
                Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
                if (argument instanceof Class) return Optional.of(((Class<?>) argument));
            }
        }
        return Optional.empty();
    }

}
