package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.PositionComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.ColorfulStringComponent;

import java.util.List;

public class GuiHud {
    protected List<GuiValueBar> valueBarList;
    protected List<PositionComponent<ColorfulStringComponent>> stringPositionComponentList;

    public GuiHud(List<GuiValueBar> valueBarList, List<PositionComponent<ColorfulStringComponent>> stringPositionComponentList) {
        this.valueBarList = valueBarList;
        this.stringPositionComponentList = stringPositionComponentList;
    }

    public List<GuiValueBar> getValueBarList() {
        return valueBarList;
    }

    public void setValueBarList(List<GuiValueBar> valueBarList) {
        this.valueBarList = valueBarList;
    }

    public List<PositionComponent<ColorfulStringComponent>> getStringPositionComponentList() {
        return stringPositionComponentList;
    }

    public void setStringPositionComponentList(List<PositionComponent<ColorfulStringComponent>> stringPositionComponentList) {
        this.stringPositionComponentList = stringPositionComponentList;
    }
}
