package nand1.extralife.mixin;

import nand1.extralife.capability.HardcoreLivesProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;



@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Redirect(
            method = "handleClientCommand(Lnet/minecraft/network/protocol/game/ServerboundClientCommandPacket;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;isHardcore()Z"
            )
    )


    private boolean redirectHardcoreRespawn(MinecraftServer server) {
        ServerPlayer player = ((ServerGamePacketListenerImpl)(Object)this).player;
        if (server == null || !server.isHardcore()) return false;

        player.getCapability(HardcoreLivesProvider.CAPABILITY).ifPresent(lives ->
        player.sendSystemMessage(
                Component.literal(
                        "You have " + lives.getLives() + " lives remaining."
                )
        ));

        return player.getCapability(HardcoreLivesProvider.CAPABILITY)
                .map(l -> l.getLives() <= 0)
                .orElse(true);
    }
}

