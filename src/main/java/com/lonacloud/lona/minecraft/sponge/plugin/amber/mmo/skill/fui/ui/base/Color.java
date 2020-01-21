package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base;

public class Color {
    protected int color;

    public Color(int color) {
        this.color = color;
    }

    public enum ColorType {
        RED(new Color(0x00ff0000)),
        GREEN(new Color(0x0000ff00)),
        BLUE(new Color(0x000000ff)),
        ;
        protected Color color;

        ColorType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
