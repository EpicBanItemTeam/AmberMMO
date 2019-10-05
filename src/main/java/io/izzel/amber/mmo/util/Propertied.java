package io.izzel.amber.mmo.util;

import java.util.Optional;

public interface Propertied {

    <T> Optional<T> getProperty(String id);

    <T> void setProperty(String id, T value);

    default <T> T getUnchecked(String id) {
        return this.<T>getProperty(id).orElseThrow(NullPointerException::new);
    }

}
