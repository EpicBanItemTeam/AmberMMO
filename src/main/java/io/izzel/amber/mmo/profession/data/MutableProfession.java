package io.izzel.amber.mmo.profession.data;

import io.izzel.amber.mmo.skill.data.SkillTree;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Optional;

@NonnullByDefault
public class MutableProfession implements DataSerializable {

    private static final DataQuery ID = DataQuery.of("Id");
    private static final DataQuery EXPERIENCE = DataQuery.of("Experience");
    private static final DataQuery SKILL_TREE = DataQuery.of("SkillTree");

    private final String id;
    private SkillTree skillTree;
    private double experience;

    public MutableProfession(String id, SkillTree skillTree, double experience) {
        this.id = id;
        this.skillTree = skillTree;
        this.experience = experience;
    }

    public double getExperience() {
        return experience;
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
        return DataContainer.createNew()
            .set(ID, id)
            .set(EXPERIENCE, experience)
            .set(SKILL_TREE, skillTree)
            .set(Queries.CONTENT_VERSION, this.getContentVersion());
    }

    public static class Builder extends AbstractDataBuilder<MutableProfession> {

        public Builder() {
            super(MutableProfession.class, 0);
        }

        @Override
        protected Optional<MutableProfession> buildContent(DataView container) throws InvalidDataException {
            String id = container.getString(ID).orElseThrow(InvalidDataException::new);
            double exp = container.getDouble(EXPERIENCE).orElseThrow(InvalidDataException::new);
            SkillTree st = container.getSerializable(SKILL_TREE, SkillTree.class).orElse(SkillTree.empty());
            return Optional.of(new MutableProfession(id, st, exp));
        }
    }

}
