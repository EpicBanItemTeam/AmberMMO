package io.izzel.amber.mmo.skill.data;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@NonnullByDefault
public interface SkillTree extends DataSerializable, Iterable<SkillTree.Leaf> {

    interface Leaf extends SkillTree {

        <E extends EntitySkill> E getSkill();

        Leaf copy();

    }

    List<Leaf> getChildren();

    void setChildren(List<Leaf> children);

    SkillTree copy();

    void add(Leaf child);

    void remove(Leaf child);

    default boolean isEmpty() {
        if (this instanceof Leaf) return false;
        else return getChildren().stream().allMatch(SkillTree::isEmpty);
    }

    default Iterator<Leaf> iterator() {
        return walk().iterator();
    }

    default boolean contains(SkillTree skillTree) {
        return this.equals(skillTree) || getChildren().stream().anyMatch(it -> it.contains(skillTree));
    }

    default int size() {
        return (this instanceof Leaf ? 1 : 0) + getChildren().stream().mapToInt(SkillTree::size).sum();
    }

    default Stream<Leaf> stream() {
        return walk();
    }

    default Stream<Leaf> walk() {
        return walk(Integer.MAX_VALUE);
    }

    default Stream<Leaf> walk(int maxDepth) {
        if (maxDepth == 0) {
            if (this instanceof Leaf) {
                return Stream.of(((Leaf) this));
            } else {
                return Stream.empty();
            }
        } else {
            if (this instanceof Leaf) {
                return Stream.concat(Stream.of(((Leaf) this)), getChildren().stream().flatMap(it -> it.walk(maxDepth - 1)));
            } else {
                return getChildren().stream().flatMap(it -> it.walk(maxDepth - 1));
            }
        }
    }

    default SkillTree merge(SkillTree that) {
        if (this instanceof Leaf) {
            if (that instanceof Leaf) {
                return new SkillTreeRoot(ImmutableList.of((Leaf) this, (Leaf) that));
            } else {
                SkillTree copy = that.copy();
                copy.add((Leaf) this);
                return copy;
            }
        } else {
            if (that instanceof Leaf) {
                SkillTree copy = this.copy();
                copy.add((Leaf) that);
                return copy;
            } else {
                return new SkillTreeRoot(ImmutableList.<SkillTree.Leaf>builder()
                    .addAll(this.getChildren()).addAll(that.getChildren()).build());
            }
        }
    }

    default <E extends EntitySkill> Optional<E> find(Class<E> cl) {
        return walk().map(Leaf::getSkill).filter(cl::isInstance).map(cl::cast).findAny();
    }

    @Override
    default int getContentVersion() {
        return 0;
    }

    static SkillTree empty() {
        return new SkillTreeRoot();
    }

}
