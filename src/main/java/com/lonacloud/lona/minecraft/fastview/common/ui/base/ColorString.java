package com.lonacloud.lona.minecraft.fastview.common.ui.base;

import com.lonacloud.lona.minecraft.fastview.common.ui.gui.GuiBase;

public class ColorString extends GuiBase {
    protected String str;
    protected Color color;//ARGB

    public ColorString(boolean shouldShow, Size size, String str, Color color) {
        super(shouldShow, size);
        this.str = str;
        this.color = color;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getStr() {
        return str;
    }

    public Color getColor() {
        return color;
    }
}
