package io.github.nan8279.smcs.player;

import io.github.nan8279.smcs.CPE.AbstractExtension;
import io.github.nan8279.smcs.CPE.Extension;
import io.github.nan8279.smcs.CPE.extensions.TwoWayPingExtension;
import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.event_manager.events.MessageEvent;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
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
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.PlayerIdentificationPacket;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.*;
import io.github.nan8279.smcs.position.PlayerPosition;
import io.github.nan8279.smcs.server.Server;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * A player on the server.
 */
public class Player extends NPC {
    final private Socket socket;
    private boolean fullyJoined = false;
    private boolean supportsCPE = false;
    private String client = "Vanilla";
    private TickPlayer tickThread;
    private LocalTime pingSentTime;
    private short pingData;
    private int ping;

    /**
     * @param name the name of the player.
     * @param s the socket used to communicate with the player.
     * @param spawnPos the position the player is spawned at.
     * @param server the server the player is in.
     */
    public Player(String name, Socket s, PlayerPosition spawnPos, Server server) {
        super(name, spawnPos, server);
        socket = s;
    }

    /**
     * @return the ping with the player, in milliseconds. Only works when the player supports CPE.
     */
    public int getPing() {
        return ping;
    }

    /**
     * @return the client of the player.
     */
    public String getClient() {
        return client;
    }

    /**
     * @return if the player supports CPE.
     */
    public boolean supportsCPE() {
        return supportsCPE;
    }

    /**
     * Initializes the player.
     *
     * @param joinPacket the packet the player used to join.
     * @throws StringToBigToConvertException never.
     * @throws ClientDisconnectedException when the player disconnected.
     */
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

    /**
     * @return the IP address of the player.
     */
    public String getIPAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    /**
     * @return the join message send when the player joins.
     */
    public String getJoinMessage() {
        return Config.generateJoinMessage(this);
    }

    /**
     * Sends a packet to the player.
     *
     * @param p the packet to send.
     * @throws ClientDisconnectedException when the player disconnected.
     */
    public void send(ServerBoundPacket p) throws ClientDisconnectedException {
        NetworkUtils.sendPacket(socket, p);
    }

    /**
     * Handles packets the player send.
     *
     * @throws ClientDisconnectedException when the player disconnected.
     */
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

            if (moveEvent.isCanceled() ||
                    moveEvent.getNewPosition() != ((ClientPositionPacket) packet).getPlayerPosition()) {
                send(new ServerPositionPacket(null, moveEvent.getNewPosition()));
            }
            setPos(moveEvent.getNewPosition());
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
            } else if (((TwoWayPingExtension.TwoWayPingClientPacket) packet).getData() == pingData) {
                LocalTime currentTime = LocalTime.now();
                ping = (currentTime.getNano() - pingSentTime.getNano()) / 1000000;
            }
        }

    }

    /**
     * Sends a chat message to the player.
     *
     * @param message the message to send.
     * @throws ClientDisconnectedException when the player disconnected.
     * @throws StringToBigToConvertException when the message is too big.
     */
    public void sendMessage(String message)
            throws ClientDisconnectedException, StringToBigToConvertException {
        send(new ServerMessagePacket(message));
    }

    /**
     * Receives a packet from the player.
     *
     * @return the packet received.
     * @throws InvalidPacketException when the player send an invalid packet.
     * @throws TimeoutReachedException when the timeout has been reached.
     * @throws InvalidPacketIDException when the player send an invalid packet.
     * @throws IOException when an error occurred while sending the packet to the player.
     */
    private ClientBoundPacket receive() throws InvalidPacketException,
            TimeoutReachedException, InvalidPacketIDException, IOException {
        return NetworkUtils.readPacket(socket, 3000);
    }

    /**
     * @return true when the player is fully initialized.
     */
    public boolean isFullyJoined() {
        return fullyJoined;
    }

    /**
     * @param fullyJoined if the player is fully initialized.
     */
    private void setFullyJoined(boolean fullyJoined) {
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

    /**
     * Starts the player tick thread.
     */
    public void startTickThread() {
        tickThread = new TickPlayer(this);
        tickThread.start();
    }

    /**
     * Stops the player tick thread.
     */
    public void stopTickThread() {
        tickThread.interrupt();
    }

    /**
     * Sends a ping packet to the player.
     *
     * @throws ClientDisconnectedException when the player disconnected.
     */
    void sendPing() throws ClientDisconnectedException {
        if (supportsCPE()) {
            TwoWayPingExtension.TwoWayPingServerPacket pingPacket =
                    new TwoWayPingExtension.TwoWayPingServerPacket();

            pingData = pingPacket.getData();
            pingSentTime = LocalTime.now();
            send(pingPacket);
        } else {
            send(new PingPacket());
        }
    }
}

/**
 * Used to tick the player.
 *
 * This executes in a separate thread for each player to increase server performance.
 */
class TickPlayer extends Thread {
    final private Player player;
    private boolean needsTick = false;
    private int pingTick = 0;

    /**
     * @param player the player to tick.
     */
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

            if (pingTick == 10) {
                try {
                    player.sendPing();
                } catch (ClientDisconnectedException e) {
                    try {
                        player.disconnect(Config.DEFAULT_DISCONNECT_REASON, false);
                    } catch (StringToBigToConvertException ignored){}
                    return;
                }
                pingTick = 0;
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
            pingTick++;
        }
    }

    /**
     * Ticks the player.
     */
    void tick() {
        this.needsTick = true;
    }
}
