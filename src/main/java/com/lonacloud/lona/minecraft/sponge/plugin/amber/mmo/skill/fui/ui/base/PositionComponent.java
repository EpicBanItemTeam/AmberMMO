package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.IGuiComponent;

public class PositionComponent<T extends IGuiComponent> {
    protected Position position;
    protected T component;

    public PositionComponent(Position position, T component) {
        this.position = position;
        this.component = component;
    }

    public Position getPosition() {
        return position;
    }

    public T getComponent() {
        return component;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setComponent(T component) {
        this.component = component;
    }
}
