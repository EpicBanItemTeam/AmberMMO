package io.izzel.amber.mmo.skill;

import com.google.inject.ImplementedBy;
import io.izzel.amber.mmo.skill.storage.StoredSkill;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;

import java.util.Optional;

@ImplementedBy(SkillServiceImpl.class)
public interface SkillService {

    <S extends StoredSkill> Optional<S> getStored(String id);

    Optional<SkillSubject> getSubject(Entity entity);

    Optional<SkillSubject> getOrCreate(Entity entity);

    static SkillService instance() {
        return Sponge.getServiceManager().provideUnchecked(SkillService.class);
    }

}
