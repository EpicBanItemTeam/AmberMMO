package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.PositionComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Size;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.ColorfulStringComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.TextureComponent;

public class GuiSkill extends GuiBase {
    //技能纹理
    protected PositionComponent<TextureComponent> texturePositionComponent;
    //技能快捷键
    protected PositionComponent<ColorfulStringComponent> skillShortKeyNamePositionComponent;

    public GuiSkill(boolean shouldShow, Size size, PositionComponent<TextureComponent> texturePositionComponent, PositionComponent<ColorfulStringComponent> skillShortKeyNamePositionComponent) {
        super(shouldShow, size);
        this.texturePositionComponent = texturePositionComponent;
        this.skillShortKeyNamePositionComponent = skillShortKeyNamePositionComponent;
    }

    public PositionComponent<TextureComponent> getTexturePositionComponent() {
        return texturePositionComponent;
    }

    public void setTexturePositionComponent(PositionComponent<TextureComponent> texturePositionComponent) {
        this.texturePositionComponent = texturePositionComponent;
    }

    public PositionComponent<ColorfulStringComponent> getSkillShortKeyNamePositionComponent() {
        return skillShortKeyNamePositionComponent;
    }

    public void setSkillShortKeyNamePositionComponent(PositionComponent<ColorfulStringComponent> skillShortKeyNamePositionComponent) {
        this.skillShortKeyNamePositionComponent = skillShortKeyNamePositionComponent;
    }
}
