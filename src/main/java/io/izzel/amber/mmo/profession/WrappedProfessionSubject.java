package io.izzel.amber.mmo.profession;

import com.google.common.collect.ImmutableList;
import io.izzel.amber.mmo.profession.data.EntityProfessionData;
import io.izzel.amber.mmo.profession.data.EntityProfessionImpl;
import io.izzel.amber.mmo.skill.data.SkillTree;

import java.util.List;

final class WrappedProfessionSubject implements ProfessionSubject {

    private final EntityProfessionData.Mutable mutable;

    WrappedProfessionSubject(EntityProfessionData.Mutable mutable) {
        this.mutable = mutable;
    }

    @Override
    public List<EntityProfession> getProfessions() {
        return ImmutableList.copyOf(mutable.getProfessions().values());
    }

    @Override
    public EntityProfession add(String id) {
        EntityProfessionImpl entityProfessionImpl = new EntityProfessionImpl(id, SkillTree.empty(), 0D);
        this.mutable.update(entityProfessionImpl);
        return entityProfessionImpl;
    }

    @Override
    public boolean remove(String id) {
        return this.mutable.remove(id) != null;
    }
}
