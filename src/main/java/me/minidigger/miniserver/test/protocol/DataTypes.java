package me.minidigger.miniserver.test.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import me.minidigger.miniserver.test.model.Position;

public class DataTypes {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static int readVarInt(ByteBuf buf) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static void writeVarInt(int value, ByteBuf buf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buf.writeByte(temp);
        } while (value != 0);
    }

    public static long readVarLong(ByteBuf buf) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static void writeVarLong(long value, ByteBuf buf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buf.writeByte(temp);
        } while (value != 0);
    }

    public static String readString(ByteBuf buf) {
        int length = readVarInt(buf);

        byte[] bytes = new byte[length];
        buf.readBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeString(String string, ByteBuf buf) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length, buf);
        buf.writeBytes(bytes);
    }

    public static short readShort(ByteBuf buf) {
        return buf.readShort();
    }

    public static void writeShort(short val, ByteBuf buf) {
        buf.writeShort(val);
    }

    public static <T> T readJSON(ByteBuf buf, Class<T> clazz) {
        String string = readString(buf);

        return gson.fromJson(string, clazz);
    }

    public static void writeJSON(Object object, ByteBuf buf) {
        writeString(gson.toJson(object), buf);
    }

    public static byte[] readByteArray(ByteBuf buf) {
        int len = readVarInt(buf);
        byte[] data = new byte[len];
        buf.readBytes(data);
        return data;
    }

    public static void writeByteArray(byte[] arr, ByteBuf buf) {
        writeVarInt(arr.length, buf);
        buf.writeBytes(arr);
    }

    public static boolean readBoolean(ByteBuf buf) {
        byte b = buf.readByte();
        if (b == 0x1) return true;
        if (b == 0x0) return false;
        throw new IllegalStateException("Can't parse boolean " + b);
    }

    public static void writeBoolean(boolean val, ByteBuf buf) {
        buf.writeByte(val ? 0x1 : 0x0);
    }

    public static void writePosition(Position position, ByteBuf buf) {
        buf.writeDouble(position.getX());
        buf.writeDouble(position.getY());
        buf.writeDouble(position.getZ());
        buf.writeFloat(position.getYaw());
        buf.writeFloat(position.getPitch());
    }
}
