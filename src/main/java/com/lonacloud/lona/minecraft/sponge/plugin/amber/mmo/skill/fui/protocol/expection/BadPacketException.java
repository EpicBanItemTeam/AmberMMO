package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.protocol.expection;

public class BadPacketException extends Exception{
    private final byte[] packetFragment;

    public BadPacketException(byte[] packetFragment) {
        this.packetFragment = packetFragment;
    }

    public byte[] getPacketFragment() {
        return packetFragment;
    }
}
