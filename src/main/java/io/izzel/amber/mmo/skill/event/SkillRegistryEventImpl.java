package io.izzel.amber.mmo.skill.event;

import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@NonnullByDefault
public class SkillRegistryEventImpl implements SkillEvent.Registry {

    private final Cause cause;
    private final Map<String, Class<?>> map;
    private final DataManager dataManager = Sponge.getDataManager();

    public SkillRegistryEventImpl(Cause cause, Map<String, Class<?>> map) {
        this.cause = cause;
        this.map = map;
    }

    @Override
    public <T extends EntitySkill<?, ?>> void registerSkill(String typeId, Class<T> cl, AbstractDataBuilder<T> builder) {
        for (Type type : cl.getGenericInterfaces()) {
            if (type.equals(EntitySkill.class) && type instanceof ParameterizedType) {
                Type argument = ((ParameterizedType) type).getActualTypeArguments()[0];
                if (argument instanceof Class && StoredSkill.class.isAssignableFrom(((Class) argument))) {
                    map.put(typeId, ((Class) argument));
                    dataManager.registerBuilder(cl, builder);
                }
            }
        }
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

}
