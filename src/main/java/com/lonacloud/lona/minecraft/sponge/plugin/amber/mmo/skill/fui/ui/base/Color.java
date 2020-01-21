package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base;

public class Color {
    protected int color;

    public Color(int color) {
        this.color = color;
    }

    public enum Color {
        RED(new com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Color(0x00ff0000)),
        GREEN(new com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Color(0x0000ff00)),
        BLUE(new com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Color(0x000000ff)),
        ;
        protected com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Color color;

        Color(com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Color color) {
            this.color = color;
        }

        public com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Color getColor() {
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
