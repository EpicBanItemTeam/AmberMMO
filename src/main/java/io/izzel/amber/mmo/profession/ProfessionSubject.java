package io.izzel.amber.mmo.profession;

import io.izzel.amber.mmo.skill.data.SkillTree;

public interface ProfessionSubject extends Profession {

    SkillTree getSkillTree();

    double getExperience();

    void increaseExperience(double d);

    void decreaseExperience(double d);

}
