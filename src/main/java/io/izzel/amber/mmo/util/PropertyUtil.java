package io.izzel.amber.mmo.util;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class PropertyUtil {

    private static final DataQuery PROP = DataQuery.of("Properties");

    public static void writePropertyMap(DataView dataView, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            DataQuery query = PROP.then(entry.getKey());
            dataView.set(query.then("Type"), entry.getValue().getClass().getName());
            dataView.set(query.then("Value"), entry.getValue());
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static Map<String, Object> readPropertyMap(DataView dataView) {
        Map<String, Object> map = new LinkedHashMap<>();
        DataView view = dataView.getView(PROP).get();
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

    @SuppressWarnings({"unchecked"})
    public static <A> Optional<A> read(DataView view, Class<A> cl, DataQuery query) {
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
}
