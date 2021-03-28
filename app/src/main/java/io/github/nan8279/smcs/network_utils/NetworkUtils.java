package io.github.nan8279.smcs.network_utils;

import io.github.nan8279.smcs.CPE.ExtensionClientPacket;
import io.github.nan8279.smcs.exceptions.*;
import io.github.nan8279.smcs.network_utils.packets.ClientBoundPacket;
import io.github.nan8279.smcs.network_utils.packets.ClientPacket;
import io.github.nan8279.smcs.network_utils.packets.ServerBoundPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NetworkUtils {

    public static ClientBoundPacket readPacket(Socket socket, int timeout) throws IOException,
        TimeoutReachedException,
        InvalidPacketException, InvalidPacketIDException {

        DataInputStream stream = new DataInputStream(socket.getInputStream());

        int timeoutCount = 0;
        while (stream.available() <= 0) {
            if (timeoutCount > timeout) {
                throw new TimeoutReachedException();
            }
            timeoutCount++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored){}
        }

        int packetID = stream.readByte();
        ClientBoundPacket packetObj;
        try {
            packetObj = ClientPacket.getPacket(packetID).packet.getClass().getDeclaredConstructor().
                    newInstance();
        } catch (InvalidPacketIDException invalidPacketIDException) {
            try {
                packetObj = ExtensionClientPacket.getPacket(packetID).packet.getClass().getDeclaredConstructor().
                        newInstance();
            } catch (Exception exception) {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }

        Packet packet = new Packet(stream);
        packetObj.fromPacket(packet);
        return packetObj;
    }

    public static void sendPacket(Socket socket, ServerBoundPacket packet) throws ClientDisconnectedException {
        try {
            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

            ArrayList<Byte> payload = new ArrayList<>();
            payload.add(packet.returnPacketID());
            payload.addAll(packet.returnFields());

            byte[] bytes = new byte[payload.size()];
            int i = 0;
            for (Byte b : payload) {
                bytes[i] = b;
                i++;
            }

            stream.write(bytes);
        } catch (IOException exception) {
            throw new ClientDisconnectedException();
        }
    }

    public static byte[] generateString(String string) throws StringToBigToConvertException {
        if (string.length() > 64){
            throw new StringToBigToConvertException();
        }
        StringBuilder strBuilder = new StringBuilder(string);
        strBuilder.append(" ".repeat(Math.max(0, 64 - strBuilder.length())));
        string = strBuilder.toString();
        return string.getBytes(StandardCharsets.US_ASCII);
    }

    public static byte[] generateByteArray(byte[] byteArray) throws ByteArrayToBigToConvertException {
        ArrayList<Byte> newByteArray = new ArrayList<>();

        for (Byte b : byteArray) {
            newByteArray.add(b);
        }

        if (newByteArray.size() > 1024) {
            throw new ByteArrayToBigToConvertException();
        }

        for (int i = newByteArray.size(); i < 1024; i++) {
            newByteArray.add((byte) 0);
        }

        byte[] finalByteArray = new byte[1024];

        int i = 0;
        for (Byte b : newByteArray) {
            finalByteArray[i] = b;
            i++;
        }

        return finalByteArray;
    }

    public static byte[] shortToBytes(short s) {
        ByteBuffer bytes = ByteBuffer.allocate(2);
        bytes.putShort(s);
        return bytes.array();
    }

    public static byte[] intToBytes(int i) {
        ByteBuffer bytes = ByteBuffer.allocate(4);
        bytes.putInt(i);
        return bytes.array();
    }
}
