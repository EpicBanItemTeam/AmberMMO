package io.izzel.amber.mmo.skill.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NonnullByDefault
public class SkillTreeLeaf extends SkillTreeRoot implements SkillTree.Leaf {

    private static final DataQuery SKILL = DataQuery.of("EntitySkill");

    private final EntitySkill entitySkill;
    @Nullable private SkillTree parent;

    public SkillTreeLeaf(EntitySkill entitySkill) {
        this(entitySkill, ImmutableList.of());
    }

    private SkillTreeLeaf(EntitySkill entitySkill, List<SkillTree.Leaf> children) {
        super(children);
        this.entitySkill = entitySkill;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends EntitySkill> E getSkill() {
        return (E) entitySkill;
    }

    @Override
    public Leaf copy() {
        return new SkillTreeLeaf(entitySkill, getChildren());
    }

    @Override
    public SkillTree getParent() {
        return Objects.requireNonNull(parent);
    }

    void setParent(@Nullable SkillTree parent) {
        this.parent = parent;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
            .set(SKILL.then("Type"), entitySkill.getClass().getName())
            .set(SKILL.then("Value"), entitySkill);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("entitySkill", entitySkill)
            .add("children", getChildren())
            .toString();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SkillTreeLeaf that = (SkillTreeLeaf) o;
        return entitySkill.equals(that.entitySkill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), entitySkill);
    }

    public static class Builder extends AbstractDataBuilder<SkillTree> {

        public Builder() {
            super(SkillTree.class, 0);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Optional<SkillTree> buildContent(DataView container) throws InvalidDataException {
            List<SkillTree.Leaf> list = (List) container.getSerializableList(CHILDREN, SkillTree.class).orElseThrow(InvalidDataException::new);
            if (container.contains(SKILL)) {
                try {
                    Class<? extends EntitySkill> type = (Class<? extends EntitySkill>)
                        Class.forName(container.getString(SKILL.then("Type")).orElseThrow(NullPointerException::new));
                    EntitySkill skill = container.getSerializable(SKILL.then("Value"), type).orElseThrow(NullPointerException::new);
                    return Optional.of(new SkillTreeLeaf(skill, list));
                } catch (Exception e) {
                    throw new InvalidDataException(e);
                }
            } else {
                return Optional.of(new SkillTreeRoot(list));
            }
        }
    }

}
