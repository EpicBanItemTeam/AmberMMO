package io.izzel.amber.mmo.drops.types;

import com.google.common.base.MoreObjects;
import io.izzel.amber.mmo.drops.types.conditions.DropCondition;
import io.izzel.amber.mmo.drops.types.tables.DropTable;
import io.izzel.amber.mmo.drops.types.triggers.DropTrigger;

import java.util.List;

class SimpleDropRule implements DropRule {

    private final List<DropTrigger> triggers;
    private final List<DropCondition> conditions;
    private final List<DropTable> actions;

    SimpleDropRule(List<DropTrigger> triggers, List<DropCondition> conditions, List<DropTable> actions) {
        this.triggers = triggers;
        this.conditions = conditions;
        this.actions = actions;
    }

    @Override
    public List<DropTrigger> getTriggers() {
        return triggers;
    }

    @Override
    public List<DropCondition> getConditions() {
        return conditions;
    }

    @Override
    public List<DropTable> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("triggers", triggers)
            .add("conditions", conditions)
            .add("actions", actions)
            .toString();
    }
}
