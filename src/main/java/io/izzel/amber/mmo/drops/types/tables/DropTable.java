package io.izzel.amber.mmo.drops.types.tables;

import io.izzel.amber.mmo.util.Identified;

import java.util.Random;

public interface DropTable extends Identified {

    Random RANDOM = new Random();

    void accepts();

    default String getId() {
        return "";
    }

}
