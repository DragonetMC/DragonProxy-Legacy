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
package org.dragonet.proxy.network;

import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Getter;
import org.dragonet.net.packet.Protocol;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.LoginPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.packet.minecraft.PlayerActionPacket;
import org.dragonet.net.packet.minecraft.PlayerEquipmentPacket;
import org.dragonet.net.packet.minecraft.UseItemPacket;
import org.spacehq.packetlib.packet.Packet;

public class PEPacketProcessor implements Runnable {

    public final static int MAX_PACKETS_PER_CYCLE = 200;

    @Getter
    private final UpstreamSession client;

    private final Deque<byte[]> packets = new ArrayDeque<>();

    public PEPacketProcessor(UpstreamSession client) {
        this.client = client;
    }

    public void putPacket(byte[] packet) {
        packets.add(packet);
    }

    @Override
    public void run() {
        int cnt = 0;
        while (cnt < MAX_PACKETS_PER_CYCLE && !packets.isEmpty()) {
            cnt++;
            byte[] bin = packets.pop();
            PEPacket packet = Protocol.decode(bin);
            if (packet == null) {
                continue;
            }
            handlePacket(packet);
        }
    }

    public void handlePacket(PEPacket packet) {
        if (packet == null) {
            return;
        }
        if (BatchPacket.class.isAssignableFrom(packet.getClass())) {
            ((BatchPacket) packet).packets.stream().filter((pk) -> !(pk == null)).forEach((pk) -> {
                handlePacket(pk);
            });
            return;
        }
        switch (packet.pid()) {
            case PEPacketIDs.LOGIN_PACKET:
                client.onLogin((LoginPacket) packet);
                break;
            case PEPacketIDs.TEXT_PACKET:  //Login
                if (client.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null) {
                    PacketTranslatorRegister.translateToPC(client, packet);
                    break;
                }
            default:
                if (client.getDownstream() == null) {
                    break;
                }
                if (!client.getDownstream().isConnected()) {
                    break;
                }
                Packet[] translated = PacketTranslatorRegister.translateToPC(client, packet);
                if (translated == null || translated.length == 0) {
                    break;
                }
                client.getDownstream().send(translated);
                break;
        }
    }

}
