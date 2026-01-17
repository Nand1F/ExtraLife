package nand1.extralife.mixin;

import nand1.extralife.data.ClientDataLives;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @ModifyArg(
            method = "handlePlayerCombatKill",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
            ),
            index = 0
    )
    private Screen extralife$replaceDeathScreen(Screen original) {
        System.out.println("[EXTRALIFE] ModifyArg CALLED");
        if (original instanceof DeathScreen ds) {
            System.out.println("[EXTRALIFE] DeathScreen replaced");
            if(ClientDataLives.getLives() > 0){
                return new DeathScreen(ds.getTitle(), false);
            }else {
                return new DeathScreen(ds.getTitle(), true);
            }

        }
        return original;
    }
}


