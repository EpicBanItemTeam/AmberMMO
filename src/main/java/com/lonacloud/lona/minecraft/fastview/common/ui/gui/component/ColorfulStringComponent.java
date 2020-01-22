package com.lonacloud.lona.minecraft.fastview.common.ui.gui.component;

import com.lonacloud.lona.minecraft.fastview.common.ui.base.ColorfulString;

public class ColorfulStringComponent extends ComponentBase {
    protected ColorfulString colorfulString;

    public ColorfulStringComponent(boolean shouldShow) {
        super(shouldShow);
    }

    public ColorfulString getColorfulString() {
        return colorfulString;
    }

    public void setColorfulString(ColorfulString colorfulString) {
        this.colorfulString = colorfulString;
    }
}
