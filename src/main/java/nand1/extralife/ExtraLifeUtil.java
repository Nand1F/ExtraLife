package nand1.extralife;

import net.minecraft.server.MinecraftServer;

public class ExtraLifeUtil {
    public static boolean isHardcore(MinecraftServer server) {
        return server != null && server.isHardcore();
    }
}
