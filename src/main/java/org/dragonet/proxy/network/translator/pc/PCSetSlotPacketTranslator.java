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
import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;

public class PCSetSlotPacketTranslator implements PCPacketTranslator<ServerSetSlotPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSetSlotPacket packet) {
        if (!session.getWindowCache().hasWindow(packet.getWindowId())) {
            //Almost impossible to get here if window is valid and supported. 
            if(session.getDownstream() != null){
                session.getDownstream().send(new ClientCloseWindowPacket(packet.getWindowId()));    //Close non-supported windows
            }
            return null;
        }
        CachedWindow win = session.getWindowCache().get(packet.getWindowId());
        if (win.pcType == -1 && packet.getWindowId() == 0) {
            if(packet.getSlot() >= win.slots.length) return null;
            win.slots[packet.getSlot()] = packet.getItem();
            return InventoryTranslatorRegister.sendPlayerInventory(session); //Too lazy lol
        }
        return null;
    }

}
