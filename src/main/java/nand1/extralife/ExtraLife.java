package nand1.extralife;

import nand1.extralife.capability.HardcoreLives;
import nand1.extralife.capability.HardcoreLivesEvents;
import nand1.extralife.capability.HardcoreLivesProvider;
import nand1.extralife.capability.IHardcoreLives;
import nand1.extralife.commands.RegisterCommands;
import nand1.extralife.config.ModConfigs;
import nand1.extralife.network.ExtraLifeNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(ExtraLife.MOD_ID)
public class ExtraLife {

    public static final String MOD_ID = "extralife";

    public ExtraLife() {
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                ModConfigs.COMMON_SPEC
        );
        MinecraftForge.EVENT_BUS.register(HardcoreLivesEvents.class);
        MinecraftForge.EVENT_BUS.register(RegisterCommands.class);

        ExtraLifeNetwork.register();

    }

}

