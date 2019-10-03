package io.izzel.amber.mmo.profession;

import io.izzel.amber.mmo.skill.data.SkillTree;
import io.izzel.amber.mmo.util.Identified;
import io.izzel.amber.mmo.util.Propertied;

public interface EntityProfession extends Identified, Propertied {

    SkillTree getSkillTree();

    double getExperience();

    void increaseExperience(double d);

    void decreaseExperience(double d);

    default StoredProfession getStored() {
        return ProfessionService.instance().getById(this.getId()).orElseThrow(IllegalStateException::new);
    }

}
