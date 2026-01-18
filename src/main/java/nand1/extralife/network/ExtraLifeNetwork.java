package nand1.extralife.network;

import nand1.extralife.ExtraLife;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ExtraLifeNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
           new ResourceLocation(ExtraLife.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.messageBuilder(S2CLivesSyncPacket.class, id++)
                .encoder(S2CLivesSyncPacket::encode)
                .decoder(S2CLivesSyncPacket::decode)
                .consumerMainThread(S2CLivesSyncPacket::handle)
                .add();
    }
}
