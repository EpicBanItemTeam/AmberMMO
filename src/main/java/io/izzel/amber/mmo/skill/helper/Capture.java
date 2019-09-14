package io.izzel.amber.mmo.skill.helper;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;
import io.izzel.amber.mmo.skill.SkillOperation;
import io.izzel.amber.mmo.skill.SkillSubject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class Capture {

    private static Map<Class, Map<Class, Method>> methods = new HashMap<>();
    private static Map<Class, Map<String, Field>> map = new HashMap<>();

    static Map<String, Field> captureFields(Class cl) {
        return map.computeIfAbsent(cl, it -> {
            Map<String, Field> map = new HashMap<>();
            for (Field declaredField : it.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Stored.class)) {
                    declaredField.setAccessible(true);
                    String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, declaredField.getName());
                    map.put(to, declaredField);
                }
            }
            return map;
        });
    }

    static Map<Class, Method> captureMethods(Class cl) {
        return methods.computeIfAbsent(cl, it -> {
            Map<Class, Method> map = new HashMap<>();
            ImmutableSet<Method> set = ImmutableSet.<Method>builder().add(it.getDeclaredMethods()).add(it.getMethods()).build();
            for (Method declaredMethod : set) {
                if (declaredMethod.isAnnotationPresent(Operation.class)) {
                    Class<?>[] types = declaredMethod.getParameterTypes();
                    if (types.length == 2 && SkillOperation.class.isAssignableFrom(types[0])
                        && SkillSubject.CastOperator.class.isAssignableFrom(types[1])) {
                        declaredMethod.setAccessible(true);
                        map.put(types[0], declaredMethod);
                    }
                }
            }
            return map;
        });
    }

}
