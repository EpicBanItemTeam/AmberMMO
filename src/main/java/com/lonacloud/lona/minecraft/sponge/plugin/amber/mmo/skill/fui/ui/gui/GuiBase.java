package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Size;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.ComponentBase;

public abstract class GuiBase extends ComponentBase implements IGui {
    protected Size size;

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
}
