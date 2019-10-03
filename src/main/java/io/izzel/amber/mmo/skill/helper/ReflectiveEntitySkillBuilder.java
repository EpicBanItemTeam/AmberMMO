package io.izzel.amber.mmo.skill.helper;

import io.izzel.amber.mmo.util.PropertyUtil;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import static io.izzel.amber.mmo.skill.helper.AbstractEntitySkill.ID;
import static io.izzel.amber.mmo.skill.helper.ReflectiveEntitySkill.FIELDS;

@NonnullByDefault
public class ReflectiveEntitySkillBuilder<E extends ReflectiveEntitySkill> extends org.spongepowered.api.data.persistence.AbstractDataBuilder<E> {

    private final Constructor<ReflectiveEntitySkill> constructor;
    private final Class<E> requiredClass;

    @SuppressWarnings("unchecked")
    public ReflectiveEntitySkillBuilder(Class<E> requiredClass) {
        super(requiredClass, 0);
        this.requiredClass = requiredClass;
        try {
            Constructor constructor = requiredClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            this.constructor = constructor;
        } catch (Exception e) {
            throw new RuntimeException("Cannot get default constructor on " + requiredClass, e);
        }
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "unchecked"})
    @Override
    protected Optional<E> buildContent(DataView container) throws InvalidDataException {
        try {
            ReflectiveEntitySkill instance = constructor.newInstance();
            instance.setId(container.getString(ID).get());
            instance.setProp(PropertyUtil.readPropertyMap(container));
            for (Map.Entry<String, Field> entry : Capture.captureFields(requiredClass).entrySet()) {
                String key = entry.getKey();
                Field field = entry.getValue();
                DataQuery query = FIELDS.then(key);
                Optional<?> read = PropertyUtil.read(container, field.getType(), query);
                if (read.isPresent()) field.set(instance, read.get());
            }
            return Optional.of(((E) instance));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
