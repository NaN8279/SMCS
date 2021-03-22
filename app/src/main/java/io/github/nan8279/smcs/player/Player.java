package io.github.nan8279.smcs.player;

import io.github.nan8279.smcs.config.Message;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.event_manager.events.PlayerMoveEvent;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientMessagePacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientPositionPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientSetBlockPacket;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.*;
import io.github.nan8279.smcs.position.PlayerPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.*;
import java.net.Socket;

public class Player extends NPC {
    final private Socket socket;
    private boolean fullyJoined = false;
    private boolean canBreakBedrock = false;
    private TickPlayer tickThread;

    public Player(String name, Socket s, PlayerPosition spawnPos, Server server) {
        super(name, spawnPos, server);
        socket = s;
    }

    public void setCanBreakBedrock(boolean canBreakBedrock) throws ClientDisconnectedException {
        this.canBreakBedrock = canBreakBedrock;
        send(new UpdateUserTypePacket(canBreakBedrock));
    }

    public boolean canBreakBedrock() {
        return canBreakBedrock;
    }

    public String getJoinMessage() {
        return Message.generateJoinMessage(this);
    }

    public void send(ServerBoundPacket p) throws ClientDisconnectedException {
        NetworkUtils.sendPacket(socket, p);
    }

    void handlePackets() throws ClientDisconnectedException {
        ClientBoundPacket packet;

        try {
            packet = receive();
        } catch (TimeoutReachedException | IOException exception) {
            throw new ClientDisconnectedException();
        } catch (InvalidPacketException | InvalidPacketIDException exception) {
            return;
        }

        if (packet instanceof ClientPositionPacket){
            PlayerMoveEvent moveEvent = new PlayerMoveEvent(
                    this,
                    packet,
                    ((ClientPositionPacket) packet).getPlayerPosition(),
                    getPos()
            );


            getServer().getEventManager().runEvent(moveEvent);

            if (moveEvent.isCancelled() ||
                    moveEvent.getNewPosition() != ((ClientPositionPacket) packet).getPlayerPosition()) {
                send(new ServerPositionPacket(null, moveEvent.getNewPosition()));
            } else {
                setPos(moveEvent.getNewPosition());
            }
        } else if (packet instanceof ClientSetBlockPacket){
            Block block = ((ClientSetBlockPacket) packet).getMode() == 1 ?
                    ((ClientSetBlockPacket) packet).getBlockHolding() : Block.AIR;

            SetBlockEvent event = new SetBlockEvent(this, packet,
                    ((ClientSetBlockPacket) packet).getBlockPosition(),
                    block);

            event = getServer().getLevel().checkPhysic(event);
            getServer().getEventManager().runEvent(event);

            if (event.isCanceled()) {
                getServer().setBlock(event.getBlockPosition(),
                        getServer().getLevel().getBlock(event.getBlockPosition()));
            } else {
                getServer().setBlock(event.getBlockPosition(), event.getBlock());
            }
        } else if (packet instanceof ClientMessagePacket){
            MessageEvent messageEvent = new MessageEvent(this, packet,
                    ((ClientMessagePacket) packet).getMessage());

            getServer().getEventManager().runEvent(messageEvent);
            if (!messageEvent.isCanceled()) {
                getServer().sendMessage(Message.generateChatMessage(this, messageEvent.getMessage()));
            }
        }

    }

    public void sendMessage(String message) throws ClientDisconnectedException, StringToBigToConvertException {
        send(new ServerMessagePacket(message));
    }

    private ClientBoundPacket receive() throws InvalidPacketException,
            TimeoutReachedException, InvalidPacketIDException, IOException {
        return NetworkUtils.readPacket(socket, 3000);
    }

    public boolean isFullyJoined() {
        return fullyJoined;
    }

    public void setFullyJoined(boolean fullyJoined) {
        this.fullyJoined = fullyJoined;
    }

    public void disconnect(String reason, boolean silent) throws StringToBigToConvertException {
        try {
            if (socket.getInetAddress().isReachable(3000)) {
                send(new DisconnectPlayerPacket(Message.generateKickMessage(reason)));
                socket.close();
            }
            getServer().removePlayer(this, reason, silent);
        } catch (ClientDisconnectedException | IOException ignored) {}
    }

    @Override
    public void tick() {
        tickThread.tick();
    }

    public void startTickThread() {
        tickThread = new TickPlayer(this);
        tickThread.start();
    }
}

class TickPlayer extends Thread {
    final private Player player;
    private boolean needsTick = false;

    public TickPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        while (!player.getServer().isStopping()) {
            if (!player.isFullyJoined() || !needsTick) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {}
                continue;
            }

            try {
                player.send(new PingPacket());
            } catch (ClientDisconnectedException e) {
                try {
                    player.disconnect(Message.DEFAULT_DISCONNECT_REASON.toString(), false);
                } catch (StringToBigToConvertException ignored){}
                return;
            }

            ServerBoundPacket packetToSend;
            try {
                player.handlePackets();
            } catch (ClientDisconnectedException e) {
                try {
                    player.disconnect(Message.DEFAULT_DISCONNECT_REASON.toString(), false);
                } catch (StringToBigToConvertException ignored){}
                return;
            }

            needsTick = false;
        }
    }

    void tick() {
        this.needsTick = true;
    }
}
