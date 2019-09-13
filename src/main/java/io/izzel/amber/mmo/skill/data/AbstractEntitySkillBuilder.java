package io.izzel.amber.mmo.skill.data;

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

import static io.izzel.amber.mmo.skill.data.AbstractEntitySkill.ID;
import static io.izzel.amber.mmo.skill.data.AbstractEntitySkill.PROP;

@NonnullByDefault
public abstract class AbstractEntitySkillBuilder<T extends DataSerializable> extends AbstractDataBuilder<T> {

    protected AbstractEntitySkillBuilder(Class<T> requiredClass) {
        super(requiredClass, 0);
    }

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "unchecked"})
    protected Object getEntityInfo(DataView container) {
        String id = container.getString(ID).get();
        Map<String, Object> map = new LinkedHashMap<>();
        DataView view = container.getView(PROP).get();
        for (DataQuery key : view.getKeys(false)) {
            try {
                String type = view.getString(key.then("Type")).get();
                Class<?> cl = Class.forName(type);
                if (DataSerializable.class.isAssignableFrom(cl)) {
                    map.put(key.toString(), view.getSerializable(key.then("Value"), (Class) cl));
                } else {
                    if (cl.equals(Double.class)) {
                        map.put(key.toString(), view.getDouble(key.then("Value")));
                    } else if (cl.equals(Float.class)) {
                        map.put(key.toString(), view.getFloat(key.then("Value")));
                    } else if (cl.equals(Integer.class)) {
                        map.put(key.toString(), view.getInt(key.then("Value")));
                    } else if (cl.equals(Long.class)) {
                        map.put(key.toString(), view.getLong(key.then("Value")));
                    } else if (cl.equals(String.class)) {
                        map.put(key.toString(), view.getString(key.then("Value")));
                    } else if (cl.equals(Boolean.class)) {
                        map.put(key.toString(), view.getBoolean(key.then("Value")));
                    } else if (cl.equals(List.class)) {
                        map.put(key.toString(), view.getList(key.then("Value")));
                    }
                }
            } catch (Exception e) {
                throw new InvalidDataException(e);
            }
        }
        return Tuple.of(id, map);
    }
}
