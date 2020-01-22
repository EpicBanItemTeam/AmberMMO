package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.resource;

public abstract class ResourceBase implements IResource {
    protected DataLocation dataLocation;

    public ResourceBase(DataLocation dataLocation) {
        this.dataLocation = dataLocation;
    }

    @Override
    public DataLocation getDataLocation() {
        return dataLocation;
    }
}
