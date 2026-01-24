package nand1.extralife.network;
import nand1.extralife.ExtraLife;
import nand1.extralife.network.S2CLivesSyncPacket;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

public class ExtraLifeNetwork {

    public static final SimpleChannel CHANNEL = ChannelBuilder
            .named(new ResourceLocation(ExtraLife.MOD_ID, "main"))
            .networkProtocolVersion(1)
            .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(S2CLivesSyncPacket.class)
                .encoder(S2CLivesSyncPacket::encode)
                .decoder(S2CLivesSyncPacket::decode)
                .consumerMainThread(S2CLivesSyncPacket::handle)
                .add();
    }
}
