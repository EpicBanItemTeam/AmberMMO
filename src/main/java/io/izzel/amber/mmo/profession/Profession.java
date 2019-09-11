package io.izzel.amber.mmo.profession;

import org.spongepowered.api.text.Text;

import java.util.List;

public interface Profession {

    String id();

    List<String> getTags();

    default boolean hasTag(String tagName) {
        return getTags().contains(tagName);
    }

    Text getName();

    List<Text> getDescription();

}
