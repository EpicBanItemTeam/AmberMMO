package io.izzel.amber.mmo.drops.types.triggers;

import io.izzel.amber.mmo.drops.types.DropRule;

public interface DropTrigger {

    void set(DropRule rule);

    void unset();

}
