package com.lonacloud.lona.minecraft.fastview.common.protocol;

import com.lonacloud.lona.minecraft.fastview.common.protocol.expection.BadPacketException;
import com.lonacloud.lona.minecraft.fastview.common.protocol.packet.IPacket;

import java.util.List;

public interface IProtocol {
    byte[] send(IPacket packet);

    List<IPacket> recv(byte[] in) throws BadPacketException;
}
