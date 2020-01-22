package com.lonacloud.lona.minecraft.fastview.common.resource;

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
