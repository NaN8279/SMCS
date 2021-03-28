package io.github.nan8279.smcs.network_utils;

import io.github.nan8279.smcs.exceptions.InvalidPacketException;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Packet {
    final private DataInputStream stream;

    public Packet(DataInputStream stream) {
        this.stream = stream;
    }

    public byte readByte() throws InvalidPacketException {
        try {
            return stream.readByte();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    public short readShort() throws InvalidPacketException {
        try {
            return stream.readShort();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    public int readInt() throws InvalidPacketException {
        try {
            return stream.readInt();
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

    public byte[] readByteArray() throws InvalidPacketException {
        try {
            byte[] byteArray = new byte[1024];
            stream.read(byteArray);
            return byteArray;
        } catch (IOException exception) {
            throw new InvalidPacketException();
        }
    }

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
