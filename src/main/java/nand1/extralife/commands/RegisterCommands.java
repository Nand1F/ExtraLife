package nand1.extralife.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import nand1.extralife.capability.HardcoreLivesProvider;
import nand1.extralife.data.ClientDataLives;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
public class RegisterCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("lives")
                        .requires(source -> source.hasPermission(2)) // only admin
                        .then(
                                Commands.literal("add")
                                        .then(
                                                Commands.argument("player", EntityArgument.player())
                                                        .then(
                                                                Commands.argument("amount", IntegerArgumentType.integer(1))
                                                                        .executes(ctx -> {
                                                                            ServerPlayer target =
                                                                                    EntityArgument.getPlayer(ctx, "player");
                                                                            int amount =
                                                                                    IntegerArgumentType.getInteger(ctx, "amount");

                                                                            target.getCapability(HardcoreLivesProvider.CAPABILITY)
                                                                                    .ifPresent(lives -> {
                                                                                        // первірка на хардкор
                                                                                        MinecraftServer server = target.getServer();
                                                                                        if (server == null || !server.isHardcore()) {
                                                                                            target.sendSystemMessage(
                                                                                                    Component.literal(
                                                                                                            "The command is not available in normal survival mode."
                                                                                                    )
                                                                                            );
                                                                                            return;
                                                                                        }


                                                                                        lives.setLives(lives.getLives() + amount);
                                                                                        ClientDataLives.setLives(lives.getLives()+amount);
                                                                                        int total = lives.getLives() + amount;
                                                                                        if(total > 0)target.setGameMode(GameType.SURVIVAL);


                                                                                        target.sendSystemMessage(
                                                                                                Component.literal(
                                                                                                        "Lives increased by " + amount +
                                                                                                                ". Total: " + lives.getLives()
                                                                                                )
                                                                                        );
                                                                                    });

                                                                            //Перевірка на хардкоре
                                                                            MinecraftServer server = target.getServer();
                                                                            if (server.isHardcore()) {
                                                                                ctx.getSource().sendSuccess(
                                                                                        () -> Component.literal("Lives added."),
                                                                                        true
                                                                                );
                                                                            }

                                                                            return 1;
                                                                        })
                                                        )
                                        )
                        )
        );
    }
}


