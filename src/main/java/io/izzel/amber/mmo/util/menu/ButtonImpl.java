package io.izzel.amber.mmo.util.menu;

import com.google.common.base.Preconditions;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

class ButtonImpl implements Menu.Button {

    private final Map<String, Object> properties;
    private final ItemStack itemStack;
    private final int index;

    final BiConsumer<ClickInventoryEvent.Primary, Menu.Button> leftClick;
    final BiConsumer<ClickInventoryEvent.Secondary, Menu.Button> rightClick;
    final BiConsumer<ClickInventoryEvent.Shift.Primary, Menu.Button> leftShift;
    final BiConsumer<ClickInventoryEvent.Shift.Secondary, Menu.Button> rightShift;

    private ButtonImpl(ItemStack itemStack, int index,
                       BiConsumer<ClickInventoryEvent.Primary, Menu.Button> leftClick,
                       BiConsumer<ClickInventoryEvent.Secondary, Menu.Button> rightClick,
                       BiConsumer<ClickInventoryEvent.Shift.Primary, Menu.Button> leftShift,
                       BiConsumer<ClickInventoryEvent.Shift.Secondary, Menu.Button> rightShift,
                       Map<String, Object> properties) {
        this.itemStack = itemStack;
        this.index = index;
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.leftShift = leftShift;
        this.rightShift = rightShift;
        this.properties = properties;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public SlotPos getSlotPos() {
        int y = (index / 9) + 1;
        int x = (index % 9) + 1;
        return new SlotPos(x, y);
    }

    @Override
    public SlotIndex getSlotIndex() {
        return new SlotIndex(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getProperty(String id) {
        return Optional.ofNullable((T) properties.get(id));
    }

    @Override
    public <T> void setProperty(String id, T value) {
        properties.put(id, value);
    }

    static class Builder implements Menu.Button.Builder {

        private final Map<String, Object> properties = new HashMap<>();
        private ItemStack itemStack;
        private int index;

        private BiConsumer<ClickInventoryEvent.Primary, Menu.Button> leftClick;
        private BiConsumer<ClickInventoryEvent.Secondary, Menu.Button> rightClick;
        private BiConsumer<ClickInventoryEvent.Shift.Primary, Menu.Button> leftShift;
        private BiConsumer<ClickInventoryEvent.Shift.Secondary, Menu.Button> rightShift;

        @Override
        public Menu.Button.Builder item(ItemStack itemStack) {
            this.itemStack = itemStack.copy();
            return this;
        }

        @Override
        public Menu.Button.Builder slotIndex(int index) {
            Preconditions.checkArgument(index >= 0 && index < 54);
            this.index = index;
            return this;
        }

        @Override
        public Menu.Button.Builder slotPos(int x, int y) {
            int index = y * 9 + x - 10;
            Preconditions.checkArgument(index >= 0 && index < 54);
            this.index = index;
            return this;
        }

        @Override
        public <T> Menu.Button.Builder withProperty(String id, T value) {
            this.properties.put(id, value);
            return this;
        }

        @Override
        public Menu.Button.Builder onLeftClick(BiConsumer<ClickInventoryEvent.Primary, Menu.Button> handler) {
            this.leftClick = handler;
            return this;
        }

        @Override
        public Menu.Button.Builder onRightClick(BiConsumer<ClickInventoryEvent.Secondary, Menu.Button> handler) {
            this.rightClick = handler;
            return this;
        }

        @Override
        public Menu.Button.Builder onLeftShiftClick(BiConsumer<ClickInventoryEvent.Shift.Primary, Menu.Button> handler) {
            this.leftShift = handler;
            return this;
        }

        @Override
        public Menu.Button.Builder onRightShiftClick(BiConsumer<ClickInventoryEvent.Shift.Secondary, Menu.Button> handler) {
            this.rightShift = handler;
            return this;
        }

        @Override
        public Menu.Button build() {
            return new ButtonImpl(itemStack, index, leftClick, rightClick, leftShift, rightShift, properties);
        }
    }

}
