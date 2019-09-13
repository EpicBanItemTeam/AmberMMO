package io.izzel.amber.mmo.skill.data;

import com.google.common.collect.ImmutableList;
import io.izzel.amber.mmo.skill.CastingSkill;
import io.izzel.amber.mmo.skill.SkillService;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NonnullByDefault
public abstract class AbstractEntitySkill<S extends StoredSkill, C extends CastingSkill, B extends AbstractDataBuilder<?>> implements EntitySkill<S, C, B> {

    static final DataQuery ID = DataQuery.of("Id");
    static final DataQuery PROP = DataQuery.of("Properties");
    private final Map<String, Object> props = new LinkedHashMap<>();
    private final String id;

    @SuppressWarnings("unchecked")
    protected AbstractEntitySkill(Object obj) {
        Tuple<String, Map<String, Object>> tuple = (Tuple<String, Map<String, Object>>) obj;
        this.id = tuple.getFirst();
        props.putAll(tuple.getSecond());
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
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew()
            .set(Queries.CONTENT_VERSION, getContentVersion())
            .set(ID, id);
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            DataQuery query = PROP.then(entry.getKey());
            container.set(query.then("Type"), entry.getValue().getClass().getName());
            container.set(query.then("Value"), entry.getValue());
        }
        return container;
    }

}
