package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.ui.resource;

public class DataLocation {
    protected String location;
    protected String sha256;
    protected LocationType locationType;

    public DataLocation(String location, String sha256, LocationType locationType) {
        this.location = location;
        this.sha256 = sha256;
        this.locationType = locationType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public enum LocationType {
        REMOTE(0),
        INTERNAL(1),
        LOCAL(2);
        private final int type;

        LocationType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static LocationType byType(int type) {
            switch (type) {
                case 0:
                    return REMOTE;
                case 1:
                    return INTERNAL;
                case 2:
                    return LOCAL;
                default:
                    throw new IllegalArgumentException("out of range");
            }
        }
    }
}
