package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.IGuiComponent;

public class PositionComponent {
    protected Position position;
    protected IGuiComponent component;

    public PositionComponent(Position position, IGuiComponent component) {
        this.position = position;
        this.component = component;
    }

    public Position getPosition() {
        return position;
    }

    public IGuiComponent getComponent() {
        return component;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setComponent(IGuiComponent component) {
        this.component = component;
    }
}
