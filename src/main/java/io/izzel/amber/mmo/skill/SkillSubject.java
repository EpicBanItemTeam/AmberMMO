package io.izzel.amber.mmo.skill;

import io.izzel.amber.mmo.skill.data.EntitySkill;
import org.spongepowered.api.entity.Entity;

import java.util.Collection;
import java.util.Optional;

/**
 * 设计为绑定生物的技能操作接口（添加，移除，暂停，冷却）
 */
public interface SkillSubject {

    Collection<CastingSkill<?>> getCastingSkills();

    <C extends CastingSkill<E>, E extends EntitySkill<?, C>>
    Collection<C> getCastingSkills(Class<E> cl);

    <C extends CastingSkill<E>, E extends EntitySkill<?, C>>
    Optional<C> operate(Class<E> cl, SkillOperation<? super C> operation) throws UnsupportedOperationException;

    <C extends CastingSkill<E>, E extends EntitySkill<?, C>>
    C operate(C skill, SkillOperation<? super C> operation) throws UnsupportedOperationException;

    boolean isValid();

    Optional<Entity> getEntity();

    @SuppressWarnings("unchecked")
    default <T extends EntitySkill> Optional<CastingSkill<T>> find(Class<T> cl) {
        return getCastingSkills(cl).stream().findAny();
    }

    interface CastOperator {

        void add(CastingSkill skill);

        void remove(CastingSkill skill);

    }

}
