package io.izzel.amber.mmo.skill.helper;

import com.google.common.collect.ImmutableList;
import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillService;
import io.izzel.amber.mmo.skill.data.EntitySkill;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import io.izzel.amber.mmo.util.PropertyUtil;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import javax.annotation.Nullable;
import java.util.*;

@NonnullByDefault
public abstract class AbstractEntitySkill<S extends StoredSkill, C extends CastingSkill> implements EntitySkill<S, C> {

    static final DataQuery ID = DataQuery.of("Id");
    static final DataQuery PROP = DataQuery.of("Properties");
    final Map<String, Object> props = new LinkedHashMap<>();
    @Nullable protected String id;

    @SuppressWarnings("unchecked")
    protected AbstractEntitySkill(@Nullable Object obj) {
        if (obj != null) {
            Tuple<String, Map<String, Object>> tuple = (Tuple<String, Map<String, Object>>) obj;
            this.id = tuple.getFirst();
            props.putAll(tuple.getSecond());
        }
    }

    @Override
    public S getSkill() {
        return SkillService.instance().<S>getStored(id).orElseThrow(NullPointerException::new);
    }

    @Override
    public List<Text> getExtendDescription() {
        // todo add default implementation
        return ImmutableList.of();
    }

    @Override
    public String getId() {
        return getSkill().getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getProperty(String id) {
        return Optional.ofNullable(((T) props.get(id)));
    }

    @Override
    public <T> void setProperty(String id, T value) {
        props.put(id, value);
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, getContentVersion())
            .set(ID, Objects.requireNonNull(id));
        PropertyUtil.writePropertyMap(container, props);
        return container;
    }

}
