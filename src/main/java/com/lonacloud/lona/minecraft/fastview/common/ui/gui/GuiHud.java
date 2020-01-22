package com.lonacloud.lona.minecraft.fastview.common.ui.gui;

import com.lonacloud.lona.minecraft.fastview.common.ui.base.Size;
import com.lonacloud.lona.minecraft.fastview.common.ui.base.PositionComponent;
import com.lonacloud.lona.minecraft.fastview.common.ui.gui.component.ColorfulStringComponent;

import java.util.Stack;

public class GuiHud extends GuiBase{
    protected Stack<PositionComponent<GuiValueBar>> valueBarPositionComponents;
    protected PositionComponent<ColorfulStringComponent> hudTitlePositionComponent;

    public GuiHud(boolean shouldShow, Size size, Stack<PositionComponent<GuiValueBar>> valueBarPositionComponents, PositionComponent<ColorfulStringComponent> hudTitlePositionComponent) {
        super(shouldShow, size);
        this.valueBarPositionComponents = valueBarPositionComponents;
        this.hudTitlePositionComponent = hudTitlePositionComponent;
    }

    public Stack<PositionComponent<GuiValueBar>> getValueBarPositionComponents() {
        return valueBarPositionComponents;
    }

    public void setValueBarPositionComponents(Stack<PositionComponent<GuiValueBar>> valueBarPositionComponents) {
        this.valueBarPositionComponents = valueBarPositionComponents;
    }

    public PositionComponent<ColorfulStringComponent> getHudTitlePositionComponent() {
        return hudTitlePositionComponent;
    }

    public void setHudTitlePositionComponent(PositionComponent<ColorfulStringComponent> hudTitlePositionComponent) {
        this.hudTitlePositionComponent = hudTitlePositionComponent;
    }
}
