package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.*;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.BarValueComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.ColorfulStringComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.TextureComponent;

import java.util.List;

public class GuiValueBar extends GuiBase {
    protected PositionComponent<TextureComponent> backgroundTexturePositionComponent;//背景纹理
    protected PositionComponent<BarValueComponent> barValuePositionComponent;
    protected List<PositionComponent<ColorfulStringComponent>> stringPositionComponentList;

    public GuiValueBar(boolean shouldShow, Size size, PositionComponent<BarValueComponent> barValuePositionComponent, List<PositionComponent<ColorfulStringComponent>> stringPositionComponentList) {
        super(shouldShow, size);
        this.barValuePositionComponent = barValuePositionComponent;
        this.stringPositionComponentList = stringPositionComponentList;
    }

    public PositionComponent<BarValueComponent> getBarValuePositionComponent() {
        return barValuePositionComponent;
    }

    public void setBarValuePositionComponent(PositionComponent<BarValueComponent> barValuePositionComponent) {
        this.barValuePositionComponent = barValuePositionComponent;
    }

    public List<PositionComponent<ColorfulStringComponent>> getStringPositionComponentList() {
        return stringPositionComponentList;
    }

    public void setStringPositionComponentList(List<PositionComponent<ColorfulStringComponent>> stringPositionComponentList) {
        this.stringPositionComponentList = stringPositionComponentList;
    }
}
