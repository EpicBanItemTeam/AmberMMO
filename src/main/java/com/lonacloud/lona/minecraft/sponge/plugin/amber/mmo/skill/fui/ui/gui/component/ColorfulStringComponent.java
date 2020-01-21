package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.ColorfulString;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.ComponentBase;

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
