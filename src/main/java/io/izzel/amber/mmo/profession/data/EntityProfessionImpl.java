package io.izzel.amber.mmo.profession.data;

import com.google.common.collect.Maps;
import io.izzel.amber.mmo.profession.EntityProfession;
import io.izzel.amber.mmo.skill.data.SkillTree;
import io.izzel.amber.mmo.util.PropertyUtil;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Map;
import java.util.Optional;

@NonnullByDefault
public class EntityProfessionImpl implements DataSerializable, EntityProfession {

    private static final DataQuery ID = DataQuery.of("Id");
    private static final DataQuery EXPERIENCE = DataQuery.of("Experience");
    private static final DataQuery SKILL_TREE = DataQuery.of("SkillTree");

    private final String id;
    private SkillTree skillTree;
    private double experience;
    private final Map<String, Object> properties;

    public EntityProfessionImpl(String id, SkillTree skillTree, double experience) {
        this(id, skillTree, experience, Maps.newLinkedHashMap());
    }

    public EntityProfessionImpl(String id, SkillTree skillTree, double experience, Map<String, Object> properties) {
        this.id = id;
        this.skillTree = skillTree;
        this.experience = experience;
        this.properties = properties;
    }

    public double getExperience() {
        return experience;
    }

    @Override
    public void increaseExperience(double d) {
        setExperience(getExperience() + d);
    }

    @Override
    public void decreaseExperience(double d) {
        setExperience(getExperience() - d);
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public SkillTree getSkillTree() {
        return skillTree;
    }

    public String getId() {
        return id;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew()
            .set(ID, id)
            .set(EXPERIENCE, experience)
            .set(SKILL_TREE, skillTree)
            .set(Queries.CONTENT_VERSION, this.getContentVersion());
        PropertyUtil.writePropertyMap(container, properties);
        return container;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getProperty(String id) {
        return Optional.ofNullable(((T) properties.get(id)));
    }

    @Override
    public <T> void setProperty(String id, T value) {
        properties.put(id, value);
    }

    public static class Builder extends AbstractDataBuilder<EntityProfessionImpl> {

        public Builder() {
            super(EntityProfessionImpl.class, 0);
        }

        @Override
        protected Optional<EntityProfessionImpl> buildContent(DataView container) throws InvalidDataException {
            String id = container.getString(ID).orElseThrow(InvalidDataException::new);
            double exp = container.getDouble(EXPERIENCE).orElseThrow(InvalidDataException::new);
            SkillTree st = container.getSerializable(SKILL_TREE, SkillTree.class).orElse(SkillTree.empty());
            Map<String, Object> map = PropertyUtil.readPropertyMap(container);
            return Optional.of(new EntityProfessionImpl(id, st, exp, map));
        }
    }

}
