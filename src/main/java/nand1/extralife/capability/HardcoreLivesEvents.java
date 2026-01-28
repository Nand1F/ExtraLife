package nand1.extralife.capability;

import nand1.extralife.config.ModConfigs;
import nand1.extralife.data.ClientDataLives;
import nand1.extralife.network.ExtraLifeNetwork;
import nand1.extralife.network.LivesPacketSender;
import nand1.extralife.network.S2CLivesSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.nio.file.Files;
import java.nio.file.Path;


public class HardcoreLivesEvents {

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayer) {
            event.addCapability(
                    ResourceLocation.fromNamespaceAndPath("extralife", "hardcore_lives"),
                    new HardcoreLivesProvider()
            );
        }
    }

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        MinecraftServer mcServer = player.getServer();
        if (mcServer == null || !mcServer.isHardcore()) {
            ClientDataLives.setIsHardcore(false);
            return;

        }else {
            ClientDataLives.setIsHardcore(true);
        }

        var server = player.getServer();
        if (server == null) return;


        Path playerDataDir = server.getWorldPath(LevelResource.PLAYER_DATA_DIR);
        Path playerFile = playerDataDir.resolve(player.getUUID().toString() + ".dat");

        boolean hasJoinedBefore = Files.exists(playerFile);

        if (!hasJoinedBefore) {
            player.sendSystemMessage(Component.literal("First time on this server."));
        } else {
            player.sendSystemMessage(Component.literal("Welcome back."));
        }

        player.getCapability(HardcoreLivesProvider.CAPABILITY).ifPresent(lives -> {
            if (!hasJoinedBefore && lives.getLives() == 0) {
                lives.setLives(ModConfigs.COMMON.hardcoreLives.get());
                LivesPacketSender.syncLivesTo(player, lives.getLives());
            }else if(hasJoinedBefore && lives.getLives() <=0){
                player.setGameMode(GameType.SPECTATOR);
                LivesPacketSender.syncLivesTo(player, lives.getLives());
            }else {
                LivesPacketSender.syncLivesTo(player, lives.getLives());
                player.setGameMode(GameType.SURVIVAL);
            }



            System.out.println("JOIN lives = " + lives.getLives());
            player.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal(
                            player.getName().getString() +
                                    " joined. Lives left: " + lives.getLives()
                    ),
                    false
            );

            player.sendSystemMessage(
                    Component.literal(
                            "You have " + lives.getLives() + " lives remaining."
                    )
            );
        });
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        MinecraftServer server = player.getServer();
        if (server == null || !server.isHardcore()) return;

        player.getCapability(HardcoreLivesProvider.CAPABILITY).ifPresent(lives -> {
            if (lives.getLives() > 0) {
                lives.removeLife();
            }

            LivesPacketSender.syncLivesTo(player, lives.getLives());

            player.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal(
                            player.getName().getString() +
                                    " died. Lives left: " + lives.getLives()
                    ),
                    false
            );
        });
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        if (!(event.getEntity() instanceof ServerPlayer newPlayer)) return;

        var server = newPlayer.getServer();
        if (server == null || !server.isHardcore()) return; // ✅ перевірка хардкору

        event.getOriginal().reviveCaps();
        try {
            event.getOriginal().getCapability(HardcoreLivesProvider.CAPABILITY).ifPresent(oldCap -> {
                newPlayer.getCapability(HardcoreLivesProvider.CAPABILITY).ifPresent(newCap -> {
                    newCap.setLives(oldCap.getLives());
                    LivesPacketSender.syncLivesTo(newPlayer, oldCap.getLives());
                    System.out.println("Cloned lives = " + newCap.getLives());
                });
            });
        } finally {
            event.getOriginal().invalidateCaps(); // ✅ гарантовано виконається
        }
    }

}
