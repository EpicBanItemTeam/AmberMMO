package io.izzel.amber.mmo.drops.types.triggers;

public interface DropTrigger {

    void set(Runnable action);

    void unset();

}
