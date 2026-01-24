package nand1.extralife.network;

import nand1.extralife.data.ClientDataLives;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;


public class S2CLivesSyncPacket {

    private final int lives;

    public S2CLivesSyncPacket(int lives) {
        this.lives = lives;
    }

    // Сервер -> пише дані в буфер
    public static void encode(S2CLivesSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.lives);
    }

    // Клієнт -> читає дані з буфера
    public static S2CLivesSyncPacket decode(FriendlyByteBuf buf) {
        return new S2CLivesSyncPacket(buf.readVarInt());
    }

    // Клієнт -> застосовує дані
    public static void handle(S2CLivesSyncPacket msg, CustomPayloadEvent.Context ctx ) {
        ctx.enqueueWork(() -> {
            ClientDataLives.setLives(msg.lives);
        });
        ctx.setPacketHandled(true);
    }

}
