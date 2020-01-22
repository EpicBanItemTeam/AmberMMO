package com.lonacloud.lona.minecraft.fastview.common.ui.gui;

import com.lonacloud.lona.minecraft.fastview.common.ui.base.Size;
import com.lonacloud.lona.minecraft.fastview.common.ui.base.PositionComponent;
import com.lonacloud.lona.minecraft.fastview.common.ui.gui.component.ComponentBase;
import com.lonacloud.lona.minecraft.fastview.common.ui.gui.component.IGuiComponent;

import java.util.Stack;

public abstract class GuiBase extends ComponentBase implements IGui {
    protected Size size;
    protected Stack<PositionComponent<IGuiComponent>> components = new Stack<>();

    public GuiBase(boolean shouldShow, Size size) {
        super(shouldShow);
        this.size = size;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public Stack<PositionComponent<IGuiComponent>> getComponents() {
        return components;
    }

    @Override
    public void setComponents(Stack<PositionComponent<IGuiComponent>> components) {
        this.components = components;
    }
}
