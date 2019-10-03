package io.izzel.amber.mmo.profession.storage;

import com.google.common.base.MoreObjects;
import io.izzel.amber.mmo.profession.StoredProfession;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class StoredProfessionImpl implements StoredProfession {

    @Setting private Text name;
    @Setting private List<String> tags;
    @Setting private List<Text> description;
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> getTags() {
        return tags == null ? tags = new ArrayList<>() : tags;
    }

    @Override
    public Text getName() {
        return name == null ? Text.of() : name;
    }

    @Override
    public List<Text> getDescription() {
        return description == null ? description = new ArrayList<>() : description;
    }

    void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .add("tags", tags)
            .add("description", description)
            .toString();
    }
}
