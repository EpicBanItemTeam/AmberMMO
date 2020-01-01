package io.izzel.amber.mmo.drops.processor;

import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.List;

public interface DropItemProcessor {

    void handle(List<ItemStackSnapshot> list);

}
