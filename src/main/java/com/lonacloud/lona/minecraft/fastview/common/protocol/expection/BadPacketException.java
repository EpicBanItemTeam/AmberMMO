package com.lonacloud.lona.minecraft.fastview.common.protocol.expection;

public class BadPacketException extends Exception{
    private final byte[] packetFragment;

    public BadPacketException(byte[] packetFragment) {
        this.packetFragment = packetFragment;
    }

    public byte[] getPacketFragment() {
        return packetFragment;
    }
}
