package io.github.nan8279.smcs.player;

import io.github.nan8279.smcs.CPE.extensions.TwoWayPingExtension;
import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.event_manager.events.PlayerMoveEvent;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.CPE.Extension;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientMessagePacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientPositionPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.ClientSetBlockPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.PlayerIdentificationPacket;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.*;
import io.github.nan8279.smcs.position.PlayerPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Player extends NPC {
    final private Socket socket;
    private boolean fullyJoined = false;
    private boolean supportsCPE = false;
    private String client = "Vanilla";
    private TickPlayer tickThread;

    public Player(String name, Socket s, PlayerPosition spawnPos, Server server) {
        super(name, spawnPos, server);
        socket = s;
    }

    public String getClient() {
        return client;
    }

    public boolean supportsCPE() {
        return supportsCPE;
    }

    public void initialize(PlayerIdentificationPacket joinPacket)
            throws StringToBigToConvertException, ClientDisconnectedException {
        ServerIdentificationPacket identificationPacket = new ServerIdentificationPacket(
                (byte) 7,
                getServer().getName(),
                getServer().getMOTD(),
                false
        );

        for (NPC connectedNPC : getServer().getOnlinePlayers()) {
            if (connectedNPC.getUsername().equals(getUsername())) {
                disconnect("Already logged in!", true);
                return;
            }
        }

        if (getServer().getBannedPeople().contains(getUsername())) {
            disconnect("You are banned.", true);
            return;
        }

        if (joinPacket.getUnusedByte() == 66 && Config.USE_CPE) {
            supportsCPE = true;
            send(new AbstractExtension.ExtInfoPacket((short) Extension.values().length));
            HashMap<String, Boolean> clientSupportedExtensions = new HashMap<>();

            for (Extension extension : Extension.values()) {
                send(new AbstractExtension.ExtEntryPacket(extension.getExtension()));
                clientSupportedExtensions.put(extension.getExtension().getName(), false);
            }

            try {
                ClientBoundPacket extInfoPacket = receive();
                assert extInfoPacket instanceof AbstractExtension.ClientExtInfoPacket;

                int clientExtensionCount = ((AbstractExtension.ClientExtInfoPacket) extInfoPacket).getExtensionCount();
                client = ((AbstractExtension.ClientExtInfoPacket) extInfoPacket).getAppName();

                if (!(clientExtensionCount >= Extension.values().length)) {
                    disconnect("Unsupported client!", true);
                    throw new ClientDisconnectedException();
                }

                for (int i = 1; i < clientExtensionCount; i++) {
                    ClientBoundPacket extEntryPacket = receive();
                    assert extEntryPacket instanceof AbstractExtension.ClientExtEntryPacket;

                    for (String extensionName : clientSupportedExtensions.keySet()) {
                        if (((AbstractExtension.ClientExtEntryPacket) extEntryPacket).
                                getExtensionName().equals(extensionName)) {
                            clientSupportedExtensions.replace(extensionName, true);
                        }
                    }
                }

                for (boolean extensionSupported : clientSupportedExtensions.values()) {
                    if (!extensionSupported) {
                        disconnect("Unsupported client!", true);
                        throw new ClientDisconnectedException();
                    }
                }
            } catch (InvalidPacketException | IOException | InvalidPacketIDException |
                    TimeoutReachedException exception) {
                throw new ClientDisconnectedException();
            }
        }

        send(identificationPacket);
        getServer().getLevel().sendWorld(this);
        setFullyJoined(true);
        startTickThread();
        getServer().addPlayer(this, getJoinMessage(), false);

        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(this, joinPacket);
        getServer().getEventManager().runEvent(playerJoinEvent);
    }

    public String getIPAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public String getJoinMessage() {
        return Config.generateJoinMessage(this);
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

            if (moveEvent.isCancelled()) {
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

            getServer().getLevel().updateBlock(event);
            getServer().getEventManager().runEvent(event);

            if (event.isCanceled()) {
                getServer().setBlock(event.getBlockPosition(),
                        getServer().getLevel().getBlock(event.getBlockPosition()));
            } else {
                getServer().setBlock(event.getBlockPosition(), event.getBlock());
            }

            getServer().getLevel().updateNeighbours(event);
        } else if (packet instanceof ClientMessagePacket){
            MessageEvent messageEvent = new MessageEvent(this, packet,
                    ((ClientMessagePacket) packet).getMessage());

            getServer().getEventManager().runEvent(messageEvent);
            if (!messageEvent.isCanceled()) {
                getServer().sendMessage(Config.generateChatMessage(this, messageEvent.getMessage()));
            }
        } else if (packet instanceof TwoWayPingExtension.TwoWayPingClientPacket) {
            if (((TwoWayPingExtension.TwoWayPingClientPacket) packet).getDirection() == 0) {
                send(new TwoWayPingExtension.TwoWayPingServerPacket(
                        (byte) 0,
                        ((TwoWayPingExtension.TwoWayPingClientPacket) packet).getData()
                ));
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
                send(new DisconnectPlayerPacket(Config.generateKickMessage(reason)));
                socket.close();
            }
        } catch (ClientDisconnectedException | IOException ignored) {}
        getServer().removePlayer(this, reason, silent);
        setFullyJoined(false);

        if (tickThread != null) {
            stopTickThread();
        }
    }

    @Override
    public void tick() {
        tickThread.tick();
    }

    public void startTickThread() {
        tickThread = new TickPlayer(this);
        tickThread.start();
    }

    public void stopTickThread() {
        tickThread.interrupt();
    }
}

class TickPlayer extends Thread {
    final private Player player;
    private boolean needsTick = false;
    private int ping = 0;

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

            if (ping == 10) {
                try {
                    if (player.supportsCPE()) {
                        player.send(new TwoWayPingExtension.TwoWayPingServerPacket());
                    } else {
                        player.send(new PingPacket());
                    }
                } catch (ClientDisconnectedException e) {
                    try {
                        player.disconnect(Config.DEFAULT_DISCONNECT_REASON, false);
                    } catch (StringToBigToConvertException ignored){}
                    return;
                }
                ping = 0;
            }

            try {
                player.handlePackets();
            } catch (ClientDisconnectedException e) {
                try {
                    player.disconnect(Config.DEFAULT_DISCONNECT_REASON, false);
                } catch (StringToBigToConvertException ignored){}
                return;
            }

            needsTick = false;
            ping++;
        }
    }

    void tick() {
        this.needsTick = true;
    }
}
