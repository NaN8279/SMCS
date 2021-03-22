package io.github.nan8279.smcs.player;

import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.*;
import io.github.nan8279.smcs.position.PlayerPosition;
import io.github.nan8279.smcs.server.Server;

import java.util.concurrent.ThreadLocalRandom;

public class NPC {
    final private String username;
    final private byte playerId;
    final private Server server;
    private PlayerPosition pos;

    public NPC(String name, PlayerPosition spawnPos, Server server) {
        username = name;
        playerId = (byte) ThreadLocalRandom.current().nextInt(128);
        pos = spawnPos;
        this.server = server;
    }

    public PlayerPosition getPos() {
        return pos;
    }

    public void setPos(PlayerPosition pos) {
        this.pos = pos;
        getServer().sendToAllClients(new ServerPositionPacket(this, pos));
    }

    public String getUsername(){
        return username;
    }

    public byte getPlayerId() {
        return playerId;
    }

    public void disconnect(String reason, boolean silent) throws StringToBigToConvertException {
        try{
            getServer().removePlayer(this, reason, silent);
        } catch (ClientDisconnectedException ignored){}
    }

    public Server getServer() {
        return server;
    }

    public void tick() {}
}
