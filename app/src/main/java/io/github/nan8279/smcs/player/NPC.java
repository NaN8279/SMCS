package io.github.nan8279.smcs.player;

import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.*;
import io.github.nan8279.smcs.position.PlayerPosition;
import io.github.nan8279.smcs.server.Server;

import java.util.concurrent.ThreadLocalRandom;

/**
 * NPC entity.
 */
public class NPC {
    final private String username;
    final private byte playerId;
    final private Server server;
    private PlayerPosition pos;

    /**
     * @param name the name of the NPC.
     * @param spawnPos the spawn position of the NPC.
     * @param server the server the NPC is in.
     */
    public NPC(String name, PlayerPosition spawnPos, Server server) {
        username = name;
        playerId = (byte) ThreadLocalRandom.current().nextInt(-127, 128);
        pos = spawnPos;
        this.server = server;
    }

    /**
     * @return the pos the NPC is at.
     */
    public PlayerPosition getPos() {
        return pos;
    }

    /**
     * Sets the position the NPC is at.
     *
     * @param pos the new NPC position.
     */
    public void setPos(PlayerPosition pos) {
        this.pos = pos;
        getServer().sendToAllClients(new ServerPositionPacket(this, pos));
    }

    /**
     * @return the username of the NPC.
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return the player ID of the NPC.
     */
    public byte getPlayerId() {
        return playerId;
    }

    /**
     * Disconnects the NPC from the server.
     *
     * @param reason the reason the NPC disconnects.
     * @param silent doesn't send a message to the server when true.
     * @throws StringToBigToConvertException when the reason is too big.
     */
    public void disconnect(String reason, boolean silent) throws StringToBigToConvertException {
        getServer().removePlayer(this, reason, silent);
    }

    /**
     * @return the server the NPC is in.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Ticks the NPC.
     */
    public void tick() {}
}
