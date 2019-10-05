package io.izzel.amber.mmo.skill.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NonnullByDefault
class SkillTreeRoot implements SkillTree {

    static final DataQuery CHILDREN = DataQuery.of("Children");

    private final List<SkillTree.Leaf> children = new ArrayList<>();
    private final List<SkillTree.Leaf> unmodifiable = Collections.unmodifiableList(children);

    SkillTreeRoot() {
        this(ImmutableList.of());
    }

    SkillTreeRoot(List<SkillTree.Leaf> list) {
        this.children.addAll(list);
    }

    @Override
    public List<Leaf> getChildren() {
        return unmodifiable;
    }

    @Override
    public void setChildren(List<Leaf> children) {
        this.children.clear();
        this.children.addAll(children);
        children.stream().filter(SkillTreeLeaf.class::isInstance).map(SkillTreeLeaf.class::cast)
            .forEach(it -> it.setParent(this));
    }

    @Override
    public SkillTree copy() {
        return new SkillTreeRoot(this.children);
    }

    @Override
    public void add(Leaf child) {
        this.children.add(child);
        if (child instanceof SkillTreeLeaf) {
            ((SkillTreeLeaf) child).setParent(this);
        }
    }

    @Override
    public void remove(Leaf child) {
        this.children.remove(child);
        if (child instanceof SkillTreeLeaf) {
            ((SkillTreeLeaf) child).setParent(null);
        }
    }

    @Override
    public SkillTree getParent() {
        return this;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
            .set(CHILDREN, children)
            .set(Queries.CONTENT_VERSION, getContentVersion());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("children", children)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillTreeRoot that = (SkillTreeRoot) o;
        return children.equals(that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
}
