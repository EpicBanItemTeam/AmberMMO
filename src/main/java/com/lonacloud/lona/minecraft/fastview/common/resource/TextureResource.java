package com.lonacloud.lona.minecraft.fastview.common.resource;

public class TextureResource extends ResourceBase {
    protected int u, v, width, height;

    public TextureResource(DataLocation dataLocation, int u, int v, int width, int height) {
        super(dataLocation);
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
    }

    public void setDataLocation(DataLocation dataLocation) {
        this.dataLocation = dataLocation;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
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
