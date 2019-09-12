package io.izzel.amber.mmo.profession;

import io.izzel.amber.mmo.profession.data.MutableProfession;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.text.Text;

import java.util.List;

final class WrappedProfessionSubject implements ProfessionSubject {

    private final Profession profession;
    private final MutableProfession mutableProfession;

    WrappedProfessionSubject(Profession profession, MutableProfession mutableProfession) {
        this.profession = profession;
        this.mutableProfession = mutableProfession;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataSerializable> T getSkillTree() {
        return ((T) mutableProfession.getSkillTree());
    }

    @Override
    public double getExperience() {
        return mutableProfession.getExperience();
    }

    @Override
    public void increaseExperience(double d) {
        mutableProfession.setExperience(getExperience() + d);
    }

    @Override
    public void decreaseExperience(double d) {
        mutableProfession.setExperience(getExperience() - d);
    }

    @Override
    public String getId() {
        return profession.getId();
    }

    @Override
    public List<String> getTags() {
        return profession.getTags();
    }

    @Override
    public Text getName() {
        return profession.getName();
    }

    @Override
    public List<Text> getDescription() {
        return profession.getDescription();
    }
}
