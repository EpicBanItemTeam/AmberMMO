package io.izzel.amber.mmo.profession;

import org.spongepowered.api.data.DataSerializable;

public interface ProfessionSubject extends Profession {

    <T extends DataSerializable> T getSkillTree();

    double getExperience();

    void increaseExperience(double d);

    void decreaseExperience(double d);

}
