package io.izzel.amber.mmo.util;

import java.util.List;

public interface Tagged {

    List<String> getTags();

    default boolean hasTag(String tagName) {
        return getTags().contains(tagName);
    }

}
