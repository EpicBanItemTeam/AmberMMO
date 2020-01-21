package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.ColorString;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.ComponentBase;

public class ColorStringComponent extends ComponentBase {
    protected ColorString colorString;

    public ColorStringComponent(boolean shouldShow) {
        super(shouldShow);
    }

    public ColorString getColorString() {
        return colorString;
    }

    public void setColorString(ColorString colorString) {
        this.colorString = colorString;
    }
}
