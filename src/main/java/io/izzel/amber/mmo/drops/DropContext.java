package io.izzel.amber.mmo.drops;

import lombok.Data;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Data
public class DropContext {
    private final Entity owner;
    private final Location<World> dropLocation;
}
