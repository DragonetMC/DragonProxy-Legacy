/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pe;

import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.packetlib.packet.Packet;

public class PEMovePlayerPacketTranslator implements PEPacketTranslator<MovePlayerPacket> {

    @Override
    public Packet[] translate(UpstreamSession session, MovePlayerPacket packet) {
        ClientPlayerPositionRotationPacket pk = new ClientPlayerPositionRotationPacket(!packet.teleport, packet.x, packet.y, packet.z, packet.yaw, packet.pitch);
        session.getEntityCache().getPlayer().x = packet.x;
        session.getEntityCache().getPlayer().y = packet.y;
        session.getEntityCache().getPlayer().z = packet.z;
        return new Packet[]{pk};
    }

}
