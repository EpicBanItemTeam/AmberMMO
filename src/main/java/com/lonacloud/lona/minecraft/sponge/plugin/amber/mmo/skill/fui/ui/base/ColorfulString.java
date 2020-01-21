package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.base;

import java.util.ArrayList;
import java.util.List;

public class ColorfulString {
    protected List<ColorString> stringList = new ArrayList<>();

    public ColorfulString() {
    }

    public ColorfulString(List<ColorString> stringList) {
        this.stringList = stringList;
    }


    public void add(ColorString string) {
        stringList.add(string);
    }

    public List<ColorString> getStringList() {
        return stringList;
    }

    public void setStringList(List<ColorString> stringList) {
        this.stringList = stringList;
    }
}
