package org.dragonet.proxy.network.translator.pc;

import org.dragonet.net.packet.minecraft.AddPlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PlayerListPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

public class PCSpawnPlayerPacketTranslator implements PCPacketTranslator<ServerSpawnPlayerPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket packet) {
        try {
            CachedEntity entity = session.getEntityCache().newPlayer(packet);

            // TODO: Do we need to register the player here ?
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.eid = entity.eid;

            for (EntityMetadata meta : packet.getMetadata()) {
                if (meta.getId() == 2) {
                    pkAddPlayer.username = meta.getValue().toString();
                    break;
                }
            }
            if (pkAddPlayer.username == null) {
                pkAddPlayer.username = "_";
            }

            pkAddPlayer.uuid = packet.getUUID();

            pkAddPlayer.x = (float) packet.getX() / 32;
            pkAddPlayer.y = (float) packet.getY() / 32;
            pkAddPlayer.z = (float) packet.getZ() / 32;
            pkAddPlayer.speedX = 0.0f;
            pkAddPlayer.speedY = 0.0f;
            pkAddPlayer.speedZ = 0.0f;
            pkAddPlayer.yaw = (packet.getYaw() / 256) * 360;
            pkAddPlayer.pitch = (packet.getPitch() / 256) * 360;

            //pkAddPlayer.metadata = EntityMetaData.getMetaDataFromPlayer((GlowPlayer) this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.getId()));
            PlayerListPacket lst = new PlayerListPacket(new PlayerListPacket.PlayerInfo(packet.getUUID(), packet.getEntityId(), pkAddPlayer.username, "Default", new byte[0]));
            //TODO: get the default skin to work.
            return new PEPacket[]{pkAddPlayer, lst};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
