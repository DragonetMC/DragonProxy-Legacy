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
import org.dragonet.net.packet.minecraft.SetPlayerGameTypePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.world.notify.ClientNotification;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;

public class PCNotifyClientPacketTranslator implements PCPacketTranslator<ServerNotifyClientPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerNotifyClientPacket packet) {
        if (packet.getNotification() == ClientNotification.CHANGE_GAMEMODE) {
            GameMode gm = (GameMode) packet.getValue();
            SetPlayerGameTypePacket pk = new SetPlayerGameTypePacket();
            if (gm == GameMode.CREATIVE) {
                pk.gamemode = 1;
            } else {
                pk.gamemode = 0;
            }
            return new PEPacket[]{pk};
        }
        return null;
    }

}
