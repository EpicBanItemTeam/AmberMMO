package io.izzel.amber.mmo.skill.skills.operations;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableMap;
import io.izzel.amber.mmo.skill.SkillOperation;
import org.spongepowered.api.entity.Entity;

import java.lang.reflect.Proxy;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "unused"})
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Map<String, Object> params = new HashMap<>();
        private final List<Class<?>> intfs = new ArrayList<>();

        private Builder() {
        }

        public Builder end() {
            return type(OperationEnd.class);
        }

        public Builder start() {
            return type(OperationStart.class);
        }

        public Builder targeted(Collection<Entity> targets) {
            return type(OperationTargeted.class).param("targets", targets);
        }

        public Builder sourced(Entity source) {
            return type(OperationSourced.class).param("source", source);
        }

        public Builder type(Class<?> intf) {
            intfs.add(intf);
            return this;
        }

        public Builder param(String name, Object value) {
            params.put(name, value);
            return this;
        }

        public <O extends SkillOperation> O build() {
            return create(params, intfs.toArray(new Class[0]));
        }

    }

}
