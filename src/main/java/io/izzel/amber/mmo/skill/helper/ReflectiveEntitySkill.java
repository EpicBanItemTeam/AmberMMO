package io.izzel.amber.mmo.skill.helper;

import com.google.common.base.Preconditions;
import io.izzel.amber.mmo.skill.SkillSubject;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@NonnullByDefault
@SuppressWarnings("unchecked")
public abstract class ReflectiveEntitySkill<S extends StoredSkill, C extends ReflectiveCastingSkill> extends AbstractEntitySkill<S, C> {

    static final DataQuery FIELDS = DataQuery.of("Fields");
    private final Constructor<ReflectiveCastingSkill> constructor;

    protected ReflectiveEntitySkill() {
        super(null);
        try {
            Type superclass = this.getClass().getGenericSuperclass();
            Preconditions.checkState(superclass instanceof ParameterizedType);
            Type casting = ((ParameterizedType) superclass).getActualTypeArguments()[1];
            Preconditions.checkState(casting instanceof Class);
            Preconditions.checkState(ReflectiveCastingSkill.class.isAssignableFrom((Class) casting));
            Constructor constructor = ((Class) casting).getDeclaredConstructor();
            constructor.setAccessible(true);
            this.constructor = constructor;
        } catch (Exception e) {
            throw new RuntimeException("Cannot capture signature of CastingSkill", e);
        }
    }

    @Override
    public C createCast(SkillSubject subject) {
        try {
            ReflectiveCastingSkill instance = constructor.newInstance();
            instance.setOwning(this);
            instance.setSubject(subject);
            return (C) instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    final void setId(String id) {
        this.id = id;
    }

    final void setProp(Map<String, Object> map) {
        this.props.putAll(map);
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        for (Map.Entry<String, Field> entry : Capture.get(this.getClass()).entrySet()) {
            String key = entry.getKey();
            Field field = entry.getValue();
            DataQuery query = FIELDS.then(key);
            try {
                container.set(query, field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return container;
    }
}
