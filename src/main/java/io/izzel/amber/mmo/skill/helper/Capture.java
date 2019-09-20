package io.izzel.amber.mmo.skill.helper;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;
import io.izzel.amber.mmo.skill.CastingSkill;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class Capture {

    private static Map<Class, Map<Predicate<Class<?>>, Method>> methods = new HashMap<>();
    private static Map<Class, Map<String, Field>> map = new HashMap<>();

    static Map<String, Field> captureFields(Class cl) {
        return map.computeIfAbsent(cl, it -> {
            Map<String, Field> map = new HashMap<>();
            for (Field declaredField : it.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(EntityData.class)) {
                    declaredField.setAccessible(true);
                    String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, declaredField.getName());
                    map.put(to, declaredField);
                }
            }
            return map;
        });
    }

    @SuppressWarnings("unchecked")
    static Map<Predicate<Class<?>>, Method> captureMethods(Class cl) {
        return methods.computeIfAbsent(cl, it -> {
            Map<Predicate<Class<?>>, Method> map = new HashMap<>();
            ImmutableSet.Builder<Method> builder = ImmutableSet.builder();
            Class cur = cl;
            while (cur != null && !cur.equals(CastingSkill.class)) {
                builder.add(cur.getDeclaredMethods());
                cur = cur.getSuperclass();
            }
            ImmutableSet<Method> set = builder.build();
            for (Method declaredMethod : set) {
                if (declaredMethod.isAnnotationPresent(Operation.class)) {
                    Type[] types = declaredMethod.getGenericParameterTypes();
                    if (types.length == 2) {
                        Type type = types[0];
                        declaredMethod.setAccessible(true);
                        if (type instanceof Class) {
                            map.put(((Class) type)::isAssignableFrom, declaredMethod);
                        } else if (type instanceof TypeVariable) {
                            Stream<Class> bounds = Arrays.stream(((TypeVariable) type).getBounds()).filter(Class.class::isInstance)
                                .map(Class.class::cast);
                            map.put(opClass -> bounds.allMatch(bound -> bound.isAssignableFrom(opClass)), declaredMethod);
                        }
                    }
                }
            }
            return map;
        });
    }

}
