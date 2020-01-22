package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.IGuiComponent;

public abstract class ComponentBase implements IGuiComponent {
    protected boolean shouldShow;

    public ComponentBase(boolean shouldShow) {
        this.shouldShow = shouldShow;
    }

    @Override
    public boolean shouldShow() {
        return shouldShow;
    }

    @Override
    public void setShouldShow(boolean shouldShow) {
        this.shouldShow = shouldShow;
    }
}
