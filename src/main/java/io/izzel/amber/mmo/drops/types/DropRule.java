package io.izzel.amber.mmo.drops.types;

import io.izzel.amber.mmo.drops.types.conditions.DropCondition;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.triggers.DropTrigger;

import java.util.List;

public interface DropRule {

    List<DropTrigger> getTriggers();

    List<DropCondition> getConditions();

    List<DropTable> getActions();

    default void apply() {
        if (getConditions().stream().allMatch(DropCondition::test)) {
            getActions().forEach(DropTable::accepts);
        }
    }

}
