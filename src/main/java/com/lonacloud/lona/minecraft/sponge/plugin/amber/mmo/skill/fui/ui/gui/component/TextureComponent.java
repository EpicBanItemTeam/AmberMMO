package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.resource.TextureResource;

public class TextureComponent extends ComponentBase {
    protected TextureResource textureResource;
    protected int width, height;
    protected boolean isBlend;
    protected float glColorRed, glColorGreen, glColorBlue, glColorAlpha;

    public TextureComponent(boolean shouldShow, TextureResource textureResource, int width, int height, boolean isBlend) {
        super(shouldShow);
        this.textureResource = textureResource;
        this.width = width;
        this.height = height;
        this.isBlend = isBlend;
        this.glColorRed = 1.0f;
        this.glColorGreen = 1.0f;
        this.glColorBlue = 1.0f;
        this.glColorAlpha = 1.0f;
    }

    public TextureComponent(boolean shouldShow, TextureResource textureResource, int width, int height, boolean isBlend, float glColorRed, float glColorGreen, float glColorBlue, float glColorAlpha) {
        super(shouldShow);
        this.textureResource = textureResource;
        this.width = width;
        this.height = height;
        this.isBlend = isBlend;
        this.glColorRed = glColorRed;
        this.glColorGreen = glColorGreen;
        this.glColorBlue = glColorBlue;
        this.glColorAlpha = glColorAlpha;
    }

    public TextureResource getTextureResource() {
        return textureResource;
    }

    public void setTextureResource(TextureResource textureResource) {
        this.textureResource = textureResource;
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

    public boolean isBlend() {
        return isBlend;
    }

    public void setBlend(boolean blend) {
        isBlend = blend;
    }

    public float getGlColorRed() {
        return glColorRed;
    }

    public void setGlColorRed(float glColorRed) {
        this.glColorRed = glColorRed;
    }

    public float getGlColorGreen() {
        return glColorGreen;
    }

    public void setGlColorGreen(float glColorGreen) {
        this.glColorGreen = glColorGreen;
    }

    public float getGlColorBlue() {
        return glColorBlue;
    }

    public void setGlColorBlue(float glColorBlue) {
        this.glColorBlue = glColorBlue;
    }

    public float getGlColorAlpha() {
        return glColorAlpha;
    }

    public void setGlColorAlpha(float glColorAlpha) {
        this.glColorAlpha = glColorAlpha;
    }
}
