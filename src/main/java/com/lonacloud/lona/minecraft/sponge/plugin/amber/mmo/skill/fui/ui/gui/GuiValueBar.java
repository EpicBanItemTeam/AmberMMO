package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.*;

public class GuiValueBar extends GuiBase {
    protected ColorfulString barName;
    protected Position barNamePosition;
    protected BarValue barValue;
    protected Position barValuePosition;


    public GuiValueBar(boolean shouldShow, Size size) {
        super(shouldShow, size);
    }
}
