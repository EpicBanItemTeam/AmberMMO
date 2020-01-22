package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.BarValue;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.resource.Texture;

public class BarValueComponent extends ComponentBase {
    protected BarValue value;
    protected Texture texture;
    protected int height;

    public BarValueComponent(boolean shouldShow, BarValue value, Texture texture, int height) {
        super(shouldShow);
        this.value = value;
        this.texture = texture;
        this.height = height;
    }

    public BarValue getValue() {
        return value;
    }

    public void setValue(BarValue value) {
        this.value = value;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
