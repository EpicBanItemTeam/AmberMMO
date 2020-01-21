package com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.protocol;

import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.protocol.expection.BadPacketException;
import com.lonacloud.lona.minecraft.sponge.plugin.amber.mmo.skill.fui.protocol.packet.IPacket;

import java.util.List;

public interface IProtocol {
    byte[] send(IPacket packet);

    List<IPacket> recv(byte[] in) throws BadPacketException;
}
