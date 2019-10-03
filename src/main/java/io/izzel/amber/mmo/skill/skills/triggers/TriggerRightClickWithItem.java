package io.izzel.amber.mmo.skill.skills.triggers;

import io.izzel.amber.mmo.skill.trigger.AbstractTrigger;
import io.izzel.amber.mmo.skill.trigger.dispatcher.Dispatcher2;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Optional;

public class TriggerRightClickWithItem extends AbstractTrigger<InteractItemEvent.Primary.MainHand, Dispatcher2<Player, ItemStackSnapshot>> {

    @Override
    public EventListener<InteractItemEvent.Primary.MainHand> getListener(Dispatcher2<Player, ItemStackSnapshot> dispatcher) {
        return event -> {
            Optional<Player> optional = event.getCause().first(Player.class);
            if (optional.isPresent()) {
                ItemStackSnapshot snapshot = event.getItemStack();
                Player player = optional.get();
                dispatcher.apply(player, snapshot);
            }
        };
    }

}
