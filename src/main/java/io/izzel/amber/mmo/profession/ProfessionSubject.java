package io.izzel.amber.mmo.profession;

import io.izzel.amber.mmo.skill.data.SkillTree;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ProfessionSubject {

    void computeProfession(Function<EntityProfession, EntityProfession> mapFunction);

    List<EntityProfession> getProfessions();

    default Optional<EntityProfession> get(String id) {
        return getProfessions().stream().filter(it -> it.getId().equals(id)).findAny();
    }

    default EntityProfession getUnchecked(String id) {
        return get(id).orElseThrow(NullPointerException::new);
    }

    default SkillTree getMerged() {
        return getProfessions().stream().map(EntityProfession::getSkillTree).reduce(SkillTree::merge).orElse(SkillTree.empty());
    }

    EntityProfession add(String id);

    boolean remove(String id);

}
