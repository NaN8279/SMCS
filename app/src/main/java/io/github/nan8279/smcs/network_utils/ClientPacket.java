package io.github.nan8279.smcs.network_utils;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Used to turn raw bytes from a packet into a {@link io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket}.
 */
public class ClientPacket {
    final private DataInputStream stream;

    /**
     * @param stream the stream the packet is on.
     */
    public ClientPacket(DataInputStream stream) {
        this.stream = stream;
    }

    /**
     * Reads a byte from the packet.
     *
     * @return the byte read.
     * @throws InvalidPacketException when an IOException occurs.
     */
    public byte readByte() throws InvalidPacketException {
        try {
            return stream.readByte();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    /**
     * Reads a short from the packet.
     *
     * @return the short read.
     * @throws InvalidPacketException when an IOException occurs.
     */
    public short readShort() throws InvalidPacketException {
        try {
            return stream.readShort();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    /**
     * Reads an integer from the packet.
     *
     * @return the integer read.
     * @throws InvalidPacketException when an IOException occurs.
     */
    public int readInt() throws InvalidPacketException {
        try {
            return stream.readInt();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    /**
     * Reads a byte array from the packet.
     *
     * @return the byte array read.
     * @throws InvalidPacketException when an IOException occurs.
     */
    public byte[] readByteArray() throws InvalidPacketException {
        try {
            byte[] byteArray = new byte[1024];
            stream.read(byteArray);
            return byteArray;
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    /**
     * Reads a string from the packet.
     *
     * @return the read string.
     * @throws InvalidPacketException when an IOException occurs.
     */
    public String readString() throws InvalidPacketException {
        try {
            byte[] stringBytes = new byte[64];
            stream.read(stringBytes);
            return new String(stringBytes, StandardCharsets.US_ASCII).trim();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }
}
