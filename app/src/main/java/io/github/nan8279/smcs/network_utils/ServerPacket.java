package io.github.nan8279.smcs.network_utils;

import io.github.nan8279.smcs.exceptions.ByteArrayToBigToConvertException;
import io.github.nan8279.smcs.exceptions.StringToBigToConvertException;

import java.util.ArrayList;

/**
 * Used to turn Java data types into raw bytes to send to the user.
 */
public class ServerPacket {
    final private ArrayList<Byte> fields = new ArrayList<>();

    /**
     * Adds a byte to the packet.
     *
     * @param b the byte to add.
     */
    public void addByte(byte b) {
        fields.add(b);
    }

    /**
     * Adds an unsigned byte to the packet.
     *
     * @param unsignedByte the unsigned byte to add.
     */
    public void addUnsignedByte(int unsignedByte) {
        fields.add((byte) unsignedByte);
    }

    /**
     * Adds a short to the packet.
     *
     * @param s the short to add.
     */
    public void addShort(short s) {
        fields.add(NetworkUtils.shortToBytes(s)[0]);
        fields.add(NetworkUtils.shortToBytes(s)[1]);
    }

    /**
     * Adds a string to the packet.
     *
     * @param s the string to add.
     * @throws StringToBigToConvertException when the string is too big to add to the packet.
     */
    public void addString(String s) throws StringToBigToConvertException {
        for (byte b : NetworkUtils.generateString(s)) {
            fields.add(b);
        }
    }

    /**
     * Adds a byte array to the packet.
     *
     * @param array the byte array to add.
     * @throws ByteArrayToBigToConvertException when the byte array is too big to add to the packet.
     */
    public void addByteArray(byte[] array) throws ByteArrayToBigToConvertException {
        for (byte b : NetworkUtils.generateByteArray(array)) {
            fields.add(b);
        }
    }

    /**
     * Adds an integer to the packet.
     *
     * @param i the integer to add.
     */
    public void addInteger(int i) {
        fields.add(NetworkUtils.intToBytes(i)[0]);
        fields.add(NetworkUtils.intToBytes(i)[1]);
        fields.add(NetworkUtils.intToBytes(i)[2]);
        fields.add(NetworkUtils.intToBytes(i)[3]);
    }

    /**
     * @return the bytes to send to the user.
     */
    public ArrayList<Byte> getFields() {
        return fields;
    }
}
