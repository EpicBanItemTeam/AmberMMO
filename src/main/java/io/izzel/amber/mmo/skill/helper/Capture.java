package io.izzel.amber.mmo.skill.helper;

import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

final class Capture {

    private static Map<Class, Map<String, Field>> map = new HashMap<>();

    static Map<String, Field> get(Class cl) {
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

}
