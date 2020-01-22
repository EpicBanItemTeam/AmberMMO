package com.lonacloud.lona.minecraft.fastview.common.ui.gui.component;

import com.lonacloud.lona.minecraft.fastview.common.ui.base.ColorString;

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
