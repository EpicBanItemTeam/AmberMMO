package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.BarValue;

public class BarValueComponent extends ComponentBase {
    protected BarValue value;
    protected TextureComponent textureComponent;
    protected int height;

    public BarValueComponent(boolean shouldShow, BarValue value, TextureComponent textureComponent, int height) {
        super(shouldShow);
        this.value = value;
        this.textureComponent = textureComponent;
        this.height = height;
    }

    public BarValue getValue() {
        return value;
    }

    public void setValue(BarValue value) {
        this.value = value;
    }

    public TextureComponent getTextureComponent() {
        return textureComponent;
    }

    public void setTextureComponent(TextureComponent textureComponent) {
        this.textureComponent = textureComponent;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
