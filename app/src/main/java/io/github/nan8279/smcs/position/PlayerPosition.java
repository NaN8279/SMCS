package io.github.nan8279.smcs.position;


public class PlayerPosition {
    final private double posX;
    final private double posY;
    final private double posZ;
    final private byte yaw;
    final private byte pitch;

    public PlayerPosition(double x, double y, double z, byte yaw, byte pitch){
        posX = x;
        posY = y;
        posZ = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public byte getYaw() {
        return yaw;
    }

    public byte getPitch() {
        return pitch;
    }

    public static PlayerPosition fromBlockPosition(BlockPosition position) {
        return new PlayerPosition(
                position.getPosX() + 0.5,
                position.getPosY() + 0.5,
                position.getPosZ() + 0.5,
                (byte) 0,
                (byte) 0);
    }
}
