package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.resource.Texture;

public class TextureComponent extends ComponentBase {
    protected Texture texture;
    protected int width, height;

    public TextureComponent(boolean shouldShow, Texture texture, int width, int height) {
        super(shouldShow);
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
