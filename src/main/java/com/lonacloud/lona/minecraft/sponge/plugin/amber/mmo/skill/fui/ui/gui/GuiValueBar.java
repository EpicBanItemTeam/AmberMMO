package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Size;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.*;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.BarValueComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.ColorfulStringComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.TextureComponent;

public class GuiValueBar extends GuiBase {
    protected PositionComponent<TextureComponent> backgroundTexturePositionComponent;//背景纹理
    protected PositionComponent<BarValueComponent> barValuePositionComponent;
    protected PositionComponent<ColorfulStringComponent> barNamePositionComponent;

    public GuiValueBar(boolean shouldShow, Size size, PositionComponent<TextureComponent> backgroundTexturePositionComponent, PositionComponent<BarValueComponent> barValuePositionComponent, PositionComponent<ColorfulStringComponent> barNamePositionComponent) {
        super(shouldShow, size);
        this.backgroundTexturePositionComponent = backgroundTexturePositionComponent;
        this.barValuePositionComponent = barValuePositionComponent;
        this.barNamePositionComponent = barNamePositionComponent;
    }

    public PositionComponent<TextureComponent> getBackgroundTexturePositionComponent() {
        return backgroundTexturePositionComponent;
    }

    public void setBackgroundTexturePositionComponent(PositionComponent<TextureComponent> backgroundTexturePositionComponent) {
        this.backgroundTexturePositionComponent = backgroundTexturePositionComponent;
    }

    public PositionComponent<BarValueComponent> getBarValuePositionComponent() {
        return barValuePositionComponent;
    }

    public void setBarValuePositionComponent(PositionComponent<BarValueComponent> barValuePositionComponent) {
        this.barValuePositionComponent = barValuePositionComponent;
    }

    public PositionComponent<ColorfulStringComponent> getBarNamePositionComponent() {
        return barNamePositionComponent;
    }

    public void setBarNamePositionComponent(PositionComponent<ColorfulStringComponent> barNamePositionComponent) {
        this.barNamePositionComponent = barNamePositionComponent;
    }
}
