package io.izzel.amber.mmo.drops.types.tables.amounts;

public interface ConditionAmount {

    boolean test();

    double map(double d);

}
