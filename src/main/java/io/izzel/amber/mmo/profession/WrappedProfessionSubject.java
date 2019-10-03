package io.izzel.amber.mmo.profession;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import io.izzel.amber.mmo.profession.data.EntityProfessionData;
import io.izzel.amber.mmo.profession.data.EntityProfessionImpl;
import io.izzel.amber.mmo.skill.data.SkillTree;
import org.spongepowered.api.entity.Entity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

final class WrappedProfessionSubject implements ProfessionSubject {

    private final EntityProfessionData.Mutable mutable;
    private final WeakReference<Entity> entityWf;

    WrappedProfessionSubject(EntityProfessionData.Mutable mutable, Entity entity) {
        this.mutable = mutable;
        this.entityWf = new WeakReference<>(entity);
    }

    @Override
    public void computeProfession(Function<EntityProfession, EntityProfession> mapFunction) {
        for (Map.Entry<String, EntityProfession> entry : this.mutable.getProfessions().entrySet()) {
            EntityProfession mapped = mapFunction.apply(entry.getValue());
            if (mapped == null) {
                this.mutable.remove(entry.getKey());
            } else {
                this.mutable.update(mapped);
            }
        }
    }

    @Override
    public List<EntityProfession> getProfessions() {
        return ImmutableList.copyOf(mutable.getProfessions().values());
    }

    @Override
    public EntityProfession add(String id) {
        Entity entity = entityWf.get();
        if (entity != null) {
            EntityProfessionImpl entityProfessionImpl = new EntityProfessionImpl(id, SkillTree.empty(), 0D);
            this.mutable.update(entityProfessionImpl);
            entity.offer(this.mutable);
            return entityProfessionImpl;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean remove(String id) {
        Entity entity = entityWf.get();
        if (entity != null) {
            EntityProfession removed = this.mutable.remove(id);
            entity.offer(this.mutable);
            return removed != null;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("mutable", mutable)
            .toString();
    }
}
