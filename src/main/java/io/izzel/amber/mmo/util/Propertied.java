package io.izzel.amber.mmo.util;

import java.util.Optional;

public interface Propertied {

    <T> Optional<T> getProperty(String id);

}
