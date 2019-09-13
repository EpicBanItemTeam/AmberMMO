package io.izzel.amber.mmo.skill.helper;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.izzel.amber.mmo.skill.helper.AbstractEntitySkill.ID;
import static io.izzel.amber.mmo.skill.helper.AbstractEntitySkill.PROP;

@NonnullByDefault
public abstract class AbstractEntitySkillBuilder<T extends DataSerializable> extends AbstractDataBuilder<T> {

    protected AbstractEntitySkillBuilder(Class<T> requiredClass) {
        super(requiredClass, 0);
    }

    @SuppressWarnings({"unchecked"})
    final <A> Optional<A> read(DataView view, Class<A> cl, DataQuery query) {
        if (DataSerializable.class.isAssignableFrom(cl)) {
            return (Optional<A>) view.getSerializable(query, (Class<? extends DataSerializable>) cl);
        } else {
            if (cl.equals(Double.class) || cl.equals(double.class)) {
                return (Optional<A>) view.getDouble(query);
            } else if (cl.equals(Float.class) || cl.equals(float.class)) {
                return (Optional<A>) view.getFloat(query);
            } else if (cl.equals(Integer.class) || cl.equals(int.class)) {
                return (Optional<A>) view.getInt(query);
            } else if (cl.equals(Long.class) || cl.equals(long.class)) {
                return (Optional<A>) view.getLong(query);
            } else if (cl.equals(String.class)) {
                return (Optional<A>) view.getString(query);
            } else if (cl.equals(Boolean.class) || cl.equals(boolean.class)) {
                return (Optional<A>) view.getBoolean(query);
            } else if (List.class.isAssignableFrom(cl)) {
                return (Optional<A>) view.getList(query);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    final Map<String, Object> readProperties(DataView container) {
        Map<String, Object> map = new LinkedHashMap<>();
        DataView view = container.getView(PROP).get();
        for (DataQuery key : view.getKeys(false)) {
            try {
                String type = view.getString(key.then("Type")).get();
                Class<?> cl = Class.forName(type);
                map.put(key.toString(), read(view, cl, key.then("Value")).get());
            } catch (Exception e) {
                throw new InvalidDataException(e);
            }
        }
        return map;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected final Object getEntityInfo(DataView container) {
        String id = container.getString(ID).get();
        return Tuple.of(id, readProperties(container));
    }
}
