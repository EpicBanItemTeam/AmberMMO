package io.izzel.amber.mmo.profession.storage;

import io.izzel.amber.mmo.profession.Profession;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;

import java.util.List;

@ConfigSerializable
public class StoredProfession implements Profession {

    @Setting private Text name;
    @Setting private List<String> tags;
    @Setting private List<Text> description;
    private String id;

    @Override
    public String id() {
        return id;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public List<Text> getDescription() {
        return description;
    }

    void setId(String id) {
        this.id = id;
    }
}
