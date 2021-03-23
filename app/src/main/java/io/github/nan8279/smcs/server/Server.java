package io.github.nan8279.smcs.server;

import io.github.nan8279.smcs.config.Config;
import io.github.nan8279.smcs.event_manager.EventManager;
import io.github.nan8279.smcs.event_manager.events.PlayerJoinEvent;
import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.level.blocks.Block;
import io.github.nan8279.smcs.logger.Logger;
import io.github.nan8279.smcs.level.ServerLevel;
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

class InitializeClient extends Thread{
    final private Server server;
    final private Player player;
    final private PlayerIdentificationPacket joinPacket;

    public InitializeClient(Player player, PlayerIdentificationPacket joinPacket){
        this.server = player.getServer();
        this.player = player;
        this.joinPacket = joinPacket;
    }

    public void run(){
        try {
            ServerIdentificationPacket identificationPacket = new ServerIdentificationPacket(
                    (byte) 7,
                    server.getName(),
                    server.getMOTD(),
                    false
            );

            for (NPC connectedNPC : server.getOnlinePlayers()) {
                if (connectedNPC.getUsername().equals(player.getUsername())) {
                    player.disconnect("Already logged in!", true);
                    return;
                }
            }

            if (server.getBannedPeople().contains(player.getUsername())) {
                player.disconnect("You are banned.", true);
                return;
            }

            player.send(identificationPacket);
            server.getLevel().sendWorldData(player);

            player.setFullyJoined(true);
            player.startTickThread();

            server.addPlayer(player, player.getJoinMessage(), false);

            PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(player, joinPacket);
            server.getEventManager().runEvent(playerJoinEvent);
        } catch (ClientDisconnectedException | StringToBigToConvertException exception) {
            server.getLogger().error("Couldn't send initialize packets to player " + player.getUsername(),
                    exception);
        }
    }
}

class CheckForNewClients extends Thread{
    final private int port;
    final private Server server;
    ServerSocket incomingSocket;

    public CheckForNewClients(Integer serverPort, Server server){
        port = serverPort;
        this.server = server;
    }

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

public class Server {
    final private int port;
    final private ArrayList<NPC> onlinePlayers = new ArrayList<>();
    final private Logger logger = new Logger();
    final private ServerLevel level;
    final private EventManager eventManager = new EventManager();
    final private ArrayList<String> bannedPeople = new ArrayList<>();
    final private String name;
    final private String MOTD;
    private boolean stopping = false;
    private CheckForNewClients clientsThread;

    public Server(ServerLevel lvl, int port, String name, String MOTD){
        this.port = port;
        this.name = name;
        this.MOTD = MOTD;
        level = lvl;
    }

    public String getName() {
        return name;
    }

    public String getMOTD() {
        return MOTD;
    }

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

    public ArrayList<String> getBannedPeople() {
        return bannedPeople;
    }

    public ArrayList<NPC> getOnlinePlayers() {
        return onlinePlayers;
    }

    public Logger getLogger() {
        return logger;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public boolean isStopping() {
        return stopping;
    }

    public void run(){
        clientsThread = new CheckForNewClients(port, this);
        clientsThread.start();
        logger.info("The server is now up on localhost:" + port);
    }

    public void stop() {
        stopping = true;
        sendMessage("Stopping server...");

        try {
            sendToAllClients(new DisconnectPlayerPacket("Server closed"));
        } catch (StringToBigToConvertException ignored) {}

        try {
            clientsThread.incomingSocket.close();
        } catch (IOException ignored) {}
    }

    public void addPlayer(NPC npc, String joinMessage, boolean silent) throws StringToBigToConvertException,
            ClientDisconnectedException {

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

    public void setBlock(BlockPosition position, Block block) {
        ServerSetBlockPacket setBlockPacket = new ServerSetBlockPacket(
                position,
                block
        );

        level.setBlock(position, block);
        sendToAllClients(setBlockPacket);
    }

    public void removePlayer(NPC npc, String reason, boolean silent) {
        onlinePlayers.remove(npc);
        sendToAllClients(new DespawnPlayerPacket(npc));
        if (!silent) {
            logger.info(npc.getUsername() + " left: " + reason);
            try {
                sendToAllClients(new ServerMessagePacket(Config.generateLeaveMessage(npc, reason)));
            } catch (StringToBigToConvertException ignored) {}
        }
    }

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

    public void tick() {
        try {
            for (NPC player : onlinePlayers) {
                player.tick();
            }
        } catch (ConcurrentModificationException ignored) {}

        level.randomTick(this);
    }
}
