package nand1.extralife.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class LivesPacketSender {
    public static void syncLivesTo(ServerPlayer player, int lives) {
        ExtraLifeNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new S2CLivesSyncPacket(lives) // твій пакет з кількома полями
        );
    }
}
