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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.RespawnPacket;
import org.dragonet.net.packet.minecraft.SetHealthPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;

public class PCUpdateHealthPacketTranslator implements PCPacketTranslator<ServerUpdateHealthPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerUpdateHealthPacket packet) {
        SetHealthPacket h = new SetHealthPacket((int) packet.getHealth());
        if (packet.getHealth() <= 0.0f) {
            RespawnPacket r = new RespawnPacket();
            r.x = (float) session.getEntityCache().getPlayer().x;
            r.y = (float) session.getEntityCache().getPlayer().y;
            r.z = (float) session.getEntityCache().getPlayer().z;
            return new PEPacket[]{h, r};
        }
        return new PEPacket[]{h};
    }

}
