package io.izzel.amber.mmo.util.menu;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

class MenuImpl implements Menu {

    private final Inventory inventory;

    public MenuImpl(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    static class Builder implements Menu.Builder {

        private final Map<Integer, ButtonImpl> buttons = new HashMap<>();

        private BiConsumer<ClickInventoryEvent.Primary, Button> globalLeftClick;
        private BiConsumer<ClickInventoryEvent.Secondary, Button> globalRightClick;
        private BiConsumer<ClickInventoryEvent.Shift.Primary, Button> globalLeftShift;
        private BiConsumer<ClickInventoryEvent.Shift.Secondary, Button> globalRightShift;
        private BiConsumer<InteractInventoryEvent.Close, Player> onClose;

        private Text title;
        private int line;

        @Override
        public Menu.Builder title(Text text) {
            this.title = text;
            return this;
        }

        @Override
        public Menu.Builder line(int size) {
            this.line = size;
            return this;
        }

        @Override
        public Menu.Builder button(Button button) {
            buttons.put(button.getSlotIndex().getValue(), ((ButtonImpl) button));
            return this;
        }

        @Override
        public Menu.Builder onLeftClick(BiConsumer<ClickInventoryEvent.Primary, Button> handler) {
            this.globalLeftClick = handler;
            return this;
        }

        @Override
        public Menu.Builder onRightClick(BiConsumer<ClickInventoryEvent.Secondary, Button> handler) {
            this.globalRightClick = handler;
            return this;
        }

        @Override
        public Menu.Builder onLeftShiftClick(BiConsumer<ClickInventoryEvent.Shift.Primary, Button> handler) {
            this.globalLeftShift = handler;
            return this;
        }

        @Override
        public Menu.Builder onRightShiftClick(BiConsumer<ClickInventoryEvent.Shift.Secondary, Button> handler) {
            this.globalRightShift = handler;
            return this;
        }

        @Override
        public Menu.Builder onClose(BiConsumer<InteractInventoryEvent.Close, Player> handler) {
            this.onClose = handler;
            return this;
        }

        @Override
        public Menu build(PluginContainer container, Player carrier) {
            Inventory inventory = Inventory.builder()
                .of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.of(title))
                .property(InventoryDimension.of(9, line))
                .listener(InteractInventoryEvent.Close.class, event ->
                    event.getCause().first(Player.class).ifPresent(player -> this.onClose.accept(event, player)))
                .listener(ClickInventoryEvent.class, event -> {
                    event.setCancelled(true);
                    if (!(event instanceof ClickInventoryEvent.Primary || event instanceof ClickInventoryEvent.Secondary))
                        return;
                    if (event instanceof ClickInventoryEvent.Drag || event instanceof ClickInventoryEvent.Double)
                        return;
                    Stream<SlotTransaction> slotTransactionStream = event.getTransactions().stream().filter(it ->
                        it.getSlot().getInventoryProperty(SlotIndex.class)
                            .map(SlotIndex::getValue).map(x -> x >= 0 && x < 54).orElse(false));
                    if (slotTransactionStream.count() > 1 || slotTransactionStream.count() == 0) return;
                    SlotTransaction transaction = slotTransactionStream.findAny().orElseThrow(IllegalStateException::new);
                    if (transaction.getOriginal().isEmpty()) return;
                    int index = transaction.getSlot().getInventoryProperty(SlotIndex.class).map(SlotIndex::getValue).orElseThrow(IllegalStateException::new);
                    ButtonImpl button = buttons.get(index);
                    if (button == null) return;
                    if (event instanceof ClickInventoryEvent.Primary) {
                        if (event instanceof ClickInventoryEvent.Shift) {
                            if (globalLeftShift != null) {
                                globalLeftShift.accept(((ClickInventoryEvent.Shift.Primary) event), buttons.get(index));
                            }
                            if (button.leftShift != null) {
                                button.leftShift.accept(((ClickInventoryEvent.Shift.Primary) event), button);
                            }
                        } else {
                            if (globalLeftClick != null) {
                                globalLeftClick.accept((ClickInventoryEvent.Primary) event, button);
                            }
                            if (button.leftClick != null) {
                                button.leftClick.accept((ClickInventoryEvent.Primary) event, button);
                            }
                        }
                    } else {
                        if (event instanceof ClickInventoryEvent.Shift) {
                            if (globalRightShift != null) {
                                globalRightShift.accept((ClickInventoryEvent.Shift.Secondary) event, button);
                            }
                            if (button.rightShift != null) {
                                button.rightShift.accept((ClickInventoryEvent.Shift.Secondary) event, button);
                            }
                        } else {
                            if (globalRightClick != null) {
                                globalRightClick.accept((ClickInventoryEvent.Secondary) event, button);
                            }
                            if (button.rightClick != null) {
                                button.rightClick.accept((ClickInventoryEvent.Secondary) event, button);
                            }
                        }
                    }
                })
                .withCarrier(carrier).build(container);
            buttons.forEach((index, button) ->
                inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).offer(button.getItemStack()));
            return new MenuImpl(inventory);
        }

    }
}
