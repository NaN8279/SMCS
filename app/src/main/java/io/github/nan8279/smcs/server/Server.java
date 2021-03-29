package io.github.nan8279.smcs.server;

import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.event_manager.EventHandler;
import io.github.nan8279.smcs.event_manager.EventManager;
import io.github.nan8279.smcs.event_manager.events.Event;
import io.github.nan8279.smcs.event_manager.events.SetBlockEvent;
import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.CPE.Extension;
import io.github.nan8279.smcs.level.ServerLevel;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.logger.Logger;
import io.github.nan8279.smcs.network_utils.NetworkUtils;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.clientbound_packets.PlayerIdentificationPacket;
import io.github.nan8279.smcs.network_utils.packets.serverbound_packets.*;
import io.github.nan8279.smcs.player.NPC;
import io.github.nan8279.smcs.player.Player;
import io.github.nan8279.smcs.position.BlockPosition;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

/**
 * Used to initialize players.
 *
 * This happens in a separate thread to increase server performance.
 */
class InitializeClient extends Thread{
    final private Server server;
    final private Player player;
    final private PlayerIdentificationPacket joinPacket;

    /**
     * @param player the player to initialize.
     * @param joinPacket the join packet the player used to join.
     */
    public InitializeClient(Player player, PlayerIdentificationPacket joinPacket){
        this.server = player.getServer();
        this.player = player;
        this.joinPacket = joinPacket;
    }

    @Override
    public void run(){
        try {
            player.initialize(joinPacket);
        } catch (ClientDisconnectedException | StringToBigToConvertException exception) {
            server.getLogger().error("Couldn't send initialize packets to player " + player.getUsername(),
                    exception);
        }
    }
}

/**
 * Checks for players wanting to join.
 *
 * This happens in a separate thread to increase server performance.
 */
class CheckForNewClients extends Thread{
    final private int port;
    final private Server server;
    ServerSocket incomingSocket;

    /**
     * @param serverPort the port the server is hosting on.
     * @param server the server.
     */
    public CheckForNewClients(int serverPort, Server server){
        port = serverPort;
        this.server = server;
    }

    @Override
    public void run(){
        while (!server.isStopping()){
            try {
                incomingSocket = new ServerSocket();
            } catch (IOException exc) {
                server.getLogger().error("An error occurred while setting up the socket.", exc);
                continue;
            }
            try{
                SocketAddress address = new InetSocketAddress("localhost", port);
                incomingSocket.bind(address);
                Socket clientSocket = incomingSocket.accept();

                ClientBoundPacket packet = NetworkUtils.readPacket(clientSocket, 3000);
                PlayerIdentificationPacket identificationPacket = (PlayerIdentificationPacket) packet;

                assert identificationPacket != null;
                Player newPlayer = new Player(identificationPacket.getUsername(),
                        clientSocket, server.getLevel().getSpawnPos(), server);
                new InitializeClient(newPlayer, identificationPacket).start();
            } catch (SocketException exception) {
                if (!server.isStopping()) {
                    server.getLogger().error(
                            "An error occurred while setting up the server socket.", exception);
                    server.stop();
                }
            } catch (IOException | TimeoutReachedException exception){
                server.getLogger().error("An error occurred while connecting with a client.", exception);
            } catch (ClassCastException exception) {
                server.getLogger().error("Someone tried to connect but sent an invalid handshake packet.",
                        exception);
            } catch (InvalidPacketIDException | InvalidPacketException ignored) {}
              finally {
                try {
                    incomingSocket.close();
                } catch (IOException exception) {
                    server.getLogger().error("An error occurred while closing the socket for incoming clients.",
                            exception);
                }
            }
        }
    }
}

/**
 * A SMCS server.
 */
public class Server {
    final private int port;
    final private ArrayList<NPC> onlinePlayers = new ArrayList<>();
    final private Logger logger = new Logger();
    final private ServerLevel level;
    final private EventManager eventManager = new EventManager();
    final private ArrayList<String> bannedPeople = new ArrayList<>();
    final private String name;
    final private String MOTD;
    final private HashMap<SetBlockEvent, Integer> delayedBlockUpdates = new HashMap<>();
    private boolean stopping = false;
    private CheckForNewClients clientsThread;

    /**
     * @param lvl the server level.
     * @param port the post the server is hosting on.
     * @param name the server's name.
     * @param MOTD the server's MOTD.
     */
    public Server(ServerLevel lvl, int port, String name, String MOTD){
        this.port = port;
        this.name = name;
        this.MOTD = MOTD;
        level = lvl;

        getEventManager().addEventHandler(new ExtensionEventCaller());
    }

    /**
     * Adds a delayed block update.
     *
     * @param blockUpdate the block update.
     * @param delay the delay in ticks.
     */
    public void addDelayedBlockUpdate(SetBlockEvent blockUpdate, int delay) {
        delayedBlockUpdates.put(blockUpdate, delay);
    }

    /**
     * @return the name of the server.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the MOTD of the server.
     */
    public String getMOTD() {
        return MOTD;
    }

    /**
     * Bans a player.
     *
     * @param playerName the player to ban.
     * @param reason the reason the player is banned.
     * @throws StringToBigToConvertException when the reason is too big.
     */
    public void ban(String playerName, String reason) throws StringToBigToConvertException {
        for (int i = 0; i < onlinePlayers.size(); i++) {
            NPC player = onlinePlayers.get(i);
            if (!(player instanceof Player)) {
                continue;
            }

            if (player.getUsername().equals(playerName)) {
                player.disconnect(reason, false);
                break;
            }
        }

        bannedPeople.add(playerName);
    }

    /**
     * @return a list of banned people.
     */
    public ArrayList<String> getBannedPeople() {
        return bannedPeople;
    }

    /**
     * @return a list of online players, including NPCs.
     */
    public ArrayList<NPC> getOnlinePlayers() {
        return onlinePlayers;
    }

    /**
     * @return the logger the server uses.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @return the level the server uses.
     */
    public ServerLevel getLevel() {
        return level;
    }

    /**
     * @return the event manager the server uses.
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * @return true if the server is stopping.
     */
    public boolean isStopping() {
        return stopping;
    }

    /**
     * Runs the server.
     */
    public void run(){
        clientsThread = new CheckForNewClients(port, this);
        clientsThread.start();
        logger.info("The server is now up on localhost:" + port);
    }

    /**
     * Stops the server.
     */
    public void stop() {
        stopping = true;
        sendMessage("Stopping server...");

        sendToAllClients(new DisconnectPlayerPacket("Server closed"));

        try {
            clientsThread.incomingSocket.close();
        } catch (IOException ignored) {}
    }

    /**
     * Adds a NPC to the server.
     *
     * @param npc the npc to add.
     * @param joinMessage the join message.
     * @param silent true when the join message must not be send.
     * @throws ClientDisconnectedException when the npc disconnected.
     */
    public void addPlayer(NPC npc, String joinMessage, boolean silent) throws ClientDisconnectedException {

        if (npc instanceof Player) {
            Player p = (Player) npc;
            p.send(new SpawnPlayerPacket(npc, true));
            for (NPC onlineNPC : onlinePlayers) {
                p.send(new SpawnPlayerPacket(onlineNPC, false));
            }
        }

        if(!silent) {
            logger.info(npc.getUsername() + " joined with player id " + npc.getPlayerId());
            sendToAllClients(new ServerMessagePacket(joinMessage));
        }
        sendToAllClients(new SpawnPlayerPacket(npc, false));
        onlinePlayers.add(npc);
    }

    /**
     * Sets a block on the server.
     * When the server is online, use this instead of {@link ServerLevel#setBlock(BlockPosition, Block)}.
     *
     * @param position the position to set the block at.
     * @param block the new block.
     */
    public void setBlock(BlockPosition position, Block block) {
        ServerSetBlockPacket setBlockPacket = new ServerSetBlockPacket(
                position,
                block
        );

        level.setBlock(position, block);
        sendToAllClients(setBlockPacket);
    }

    /**
     * Removes a NPC from the server.
     *
     * @param npc the NPC to remove.
     * @param reason the reason the NPC left.
     * @param silent true when the reason must not be send.
     */
    public void removePlayer(NPC npc, String reason, boolean silent) {
        onlinePlayers.remove(npc);
        sendToAllClients(new DespawnPlayerPacket(npc));
        if (!silent) {
            logger.info(npc.getUsername() + " left: " + reason);
            sendToAllClients(new ServerMessagePacket(Config.generateLeaveMessage(npc, reason)));
        }
    }

    /**
     * Sends a message to all online players.
     *
     * @param message the message to send.
     */
    public void sendMessage(String message) {
        try {
            for (NPC player : onlinePlayers) {
                if (player instanceof Player) {
                    try {
                        ((Player) player).sendMessage(message);
                    } catch (ClientDisconnectedException ignored) {}
                }
            }
        } catch (StringToBigToConvertException ignored) {}
        logger.info(message);
    }

    /**
     * Sends a packet to the server.
     *
     * @param packet the packet to send.
     */
    public void sendToAllClients(ServerBoundPacket packet) {
        try {
            for (NPC p : onlinePlayers) {
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    if (!(pl.isFullyJoined())) {
                        continue;
                    }
                    try {
                        pl.send(packet);
                    } catch (ClientDisconnectedException ignored) {}
                }
            }
        } catch (ConcurrentModificationException ignored) {}
    }

    /**
     * Ticks the server. Should be executed 40 times in a second (every 25 milliseconds).
     */
    public void tick() {
        try {
            for (NPC player : onlinePlayers) {
                player.tick();
            }
        } catch (ConcurrentModificationException ignored) {}

        level.randomTick(this);

        for (int i = 0; i < delayedBlockUpdates.keySet().size(); i++) {
            SetBlockEvent delayedUpdate = new ArrayList<>(delayedBlockUpdates.keySet()).get(i);

            if (delayedBlockUpdates.get(delayedUpdate) == 0) {
                level.updateBlock(delayedUpdate);
                delayedBlockUpdates.remove(delayedUpdate);
                continue;
            }
            delayedBlockUpdates.replace(delayedUpdate, delayedBlockUpdates.get(delayedUpdate) - 1);
        }
    }

    /**
     * Used to call events for extensions.
     */
    static class ExtensionEventCaller implements EventHandler {

        @Override
        public void onEvent(Event event) {
            if (event.getPlayer().supportsCPE()) {
                for (Extension extension : Extension.values()) {
                    extension.getExtension().onEvent(event);
                }
            }
        }
    }
}
