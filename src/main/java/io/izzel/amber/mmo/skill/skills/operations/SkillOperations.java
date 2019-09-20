package io.izzel.amber.mmo.skill.skills.operations;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableMap;
import io.izzel.amber.mmo.skill.SkillOperation;
import org.spongepowered.api.entity.Entity;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;

public final class SkillOperations {

    private static final Converter<String, String> CONVERTER = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    @SuppressWarnings("unchecked")
    public static <O extends SkillOperation> O create(Map<String, Object> params, Class<?>... intfs) {
        return (O) Proxy.newProxyInstance(SkillOperations.class.getClassLoader(), intfs, (proxy, method, args) -> {
            if (method.getName().startsWith("get")) {
                String convert = CONVERTER.convert(method.getName().substring(3));
                return params.get(convert);
            } else return null;
        });
    }

    public static <O extends SkillOperation> O targetedStart(Entity source, Collection<Entity> targets) {
        return create(ImmutableMap.of("source", source, "targets", targets),
            OperationTargeted.class, OperationStart.class);
    }

    public static <O extends SkillOperation> O targetedEnd(Entity source, Collection<Entity> targets) {
        return create(ImmutableMap.of("source", source, "targets", targets),
            OperationTargeted.class, OperationEnd.class);
    }

    public static <O extends SkillOperation> O lookingAt(Entity source, Collection<Entity> targets, boolean on) {
        return on ? targetedStart(source, targets) : targetedEnd(source, targets);
    }

}
