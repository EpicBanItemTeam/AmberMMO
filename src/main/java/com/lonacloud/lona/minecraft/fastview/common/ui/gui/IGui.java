package com.lonacloud.lona.minecraft.fastview.common.ui.gui;

import com.lonacloud.lona.minecraft.fastview.common.ui.gui.component.IGuiComponent;
import com.lonacloud.lona.minecraft.fastview.common.ui.base.PositionComponent;

import java.util.Stack;

public interface IGui extends IGuiComponent, IGuiSize {

    Stack<PositionComponent<IGuiComponent>> getComponents();

    void setComponents(Stack<PositionComponent<IGuiComponent>> components);
}
