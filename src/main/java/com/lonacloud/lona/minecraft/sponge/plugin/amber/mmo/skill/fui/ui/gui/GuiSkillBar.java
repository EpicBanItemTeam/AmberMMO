package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.PositionComponent;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base.Size;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.gui.component.TextureComponent;

import java.util.Stack;

public class GuiSkillBar extends GuiBase {
    protected PositionComponent<TextureComponent> backgroundTexturePositionComponent;
    protected Stack<PositionComponent<GuiSkill>> skillPositionComponents;

    public GuiSkillBar(boolean shouldShow, Size size, PositionComponent<TextureComponent> backgroundTexturePositionComponent, Stack<PositionComponent<GuiSkill>> skillPositionComponents) {
        super(shouldShow, size);
        this.backgroundTexturePositionComponent = backgroundTexturePositionComponent;
        this.skillPositionComponents = skillPositionComponents;
    }

    public PositionComponent<TextureComponent> getBackgroundTexturePositionComponent() {
        return backgroundTexturePositionComponent;
    }

    public void setBackgroundTexturePositionComponent(PositionComponent<TextureComponent> backgroundTexturePositionComponent) {
        this.backgroundTexturePositionComponent = backgroundTexturePositionComponent;
    }

    public Stack<PositionComponent<GuiSkill>> getSkillPositionComponents() {
        return skillPositionComponents;
    }

    public void setSkillPositionComponents(Stack<PositionComponent<GuiSkill>> skillPositionComponents) {
        this.skillPositionComponents = skillPositionComponents;
    }
}
