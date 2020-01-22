package com.lonacloud.lona.minecraft.fastview.common.ui.gui.component;

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
