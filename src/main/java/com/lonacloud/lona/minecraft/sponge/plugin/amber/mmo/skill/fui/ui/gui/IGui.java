package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.PositionComponent;

import java.util.Stack;

public interface IGui extends IGuiComponent, IGuiSize {

    Stack<PositionComponent<IGuiComponent>> getComponents();

    void setComponents(Stack<PositionComponent<IGuiComponent>> components);
}
