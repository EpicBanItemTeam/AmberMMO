package io.izzel.amber.mmo.drops.types.tables.amounts;

import io.izzel.amber.mmo.drops.DropContext;
import io.izzel.amber.mmo.drops.DropTableService;
import io.izzel.amber.mmo.util.Identified;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;

import java.util.Optional;

public class DynamicAmount implements Amount, Identified {

    private final String id;
    private final Amount base;

    public DynamicAmount(String id, Amount base) {
        this.id = id;
        this.base = base;
    }

    @Override
    public double get() {
        double d = base.get();
        Optional<DropContext> optional = Sponge.getCauseStackManager().getCurrentCause().first(DropContext.class);
        if (optional.isPresent()) {
            DropContext context = optional.get();
            Optional<Entity> entityOptional = context.get(DropContext.Key.OWNER);
            if (entityOptional.isPresent()) {
                Entity entity = entityOptional.get();
                return DropTableService.instance().getModifier(entity, id).applyAsDouble(d);
            }
        }
        return d;
    }

    @Override
    public String toString() {
        return String.format("[%s]", base);
    }

    @Override
    public String getId() {
        return id;
    }

}
