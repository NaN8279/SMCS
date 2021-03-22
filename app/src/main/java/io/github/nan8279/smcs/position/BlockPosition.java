package io.github.nan8279.smcs.position;

public class BlockPosition {
    final private short posX;
    final private short posY;
    final private short posZ;

    public BlockPosition(short x, short y, short z){
        posX = x;
        posY = y;
        posZ = z;
    }

    public short getPosX() {
        return posX;
    }

    public short getPosY() {
        return posY;
    }

    public short getPosZ() {
        return posZ;
    }

    public static BlockPosition fromPlayerPosition(PlayerPosition position) {
        return new BlockPosition(
                (short) position.getPosX(),
                (short) position.getPosY(),
                (short) position.getPosZ());
    }
}
