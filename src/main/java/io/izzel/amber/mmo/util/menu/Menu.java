package io.izzel.amber.mmo.util.menu;

import io.izzel.amber.mmo.util.Propertied;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.function.BiConsumer;

public interface Menu {

    void openInventory(Player player);

    static Builder builder() {
        return new MenuImpl.Builder();
    }

    interface Builder {

        Builder title(Text text);

        Builder line(int size);

        Builder button(Button button);

        Builder onLeftClick(BiConsumer<ClickInventoryEvent.Primary, Button> handler);

        Builder onRightClick(BiConsumer<ClickInventoryEvent.Secondary, Button> handler);

        Builder onLeftShiftClick(BiConsumer<ClickInventoryEvent.Shift.Primary, Button> handler);

        Builder onRightShiftClick(BiConsumer<ClickInventoryEvent.Shift.Secondary, Button> handler);

        Builder onClose(BiConsumer<InteractInventoryEvent.Close, Player> handler);

        Menu build(PluginContainer container, Player carrier);

    }

    interface Button extends Propertied {

        ItemStack getItemStack();

        SlotPos getSlotPos();

        SlotIndex getSlotIndex();

        static Builder builder() {
            return new ButtonImpl.Builder();
        }

        interface Builder {

            Builder item(ItemStack itemStack);

            Builder slotIndex(int index);

            Builder slotPos(int x, int y);

            <T> Builder withProperty(String id, T value);

            Builder onLeftClick(BiConsumer<ClickInventoryEvent.Primary, Button> handler);

            Builder onRightClick(BiConsumer<ClickInventoryEvent.Secondary, Button> handler);

            Builder onLeftShiftClick(BiConsumer<ClickInventoryEvent.Shift.Primary, Button> handler);

            Builder onRightShiftClick(BiConsumer<ClickInventoryEvent.Shift.Secondary, Button> handler);

            Button build();

        }

    }

}
