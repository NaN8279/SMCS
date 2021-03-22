# SMCS
This is a Minecraft classic server with build-in anticheat. The project is currently in Alpha stage. It is not recommended to use it in production.

# Features

Currently build-in anticheats are:

- Invalid block placement (placing a block like bedrock or placing a block too far away from your position.)
- Invalid client (this kicks users trying to use a non-vanilla client. Doesn't work with all clients.)
- Invalid movement (this moves users back to the ground when noclipping or flying.)
- Anti block spam (this kicks the user when trying to place more than 25 blocks in 5 seconds.)
- Anti chat spam (thic kicks the user when sending more than 10 messages in chat in 5 seconds.)

# Commands

Type /help in-game to get a list of available commands.

Some commands require operator status. You can give operator to a player by listing them in the `operators.txt` file in the server directory.

# Config

The anticheats are by default all disabled. They can be enabled in the server config file (`server.properties`), which can be found in the server directory.

# Technical information

## Ticks

Every 25 milliseconds (40 times in a second), the server is ticked. When the server is ticked it will tick all the players.

When a player is ticked, the server will handle the packets the player send (e.g. update player position, check if the player placed any blocks.)

### Random block tick

When the server is ticked, a specific amount of blocks in a 64 * 64 * (level height) radius from every player will be ticked.

When a block is ticked, it can do a specific action (e.g. dirt turns into grass.) What this action is depends on the block.
