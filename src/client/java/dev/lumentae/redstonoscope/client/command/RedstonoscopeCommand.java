package dev.lumentae.redstonoscope.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.lumentae.redstonoscope.client.Constants;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class RedstonoscopeCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(ClientCommands.literal(Constants.MOD_ID)
                .then(ClientCommands.literal("location")
                        .then(ClientCommands.argument("pos", BlockPosArgument.blockPos())
                                .executes(context -> {
                                    Coordinates coordinates = context.getArgument(
                                            "pos", Coordinates.class
                                    );

                                    if (!(coordinates instanceof WorldCoordinates world)) {
                                        context.getSource().sendError(
                                                Component.literal(
                                                        "Local (^) coordinates are not supported!"
                                                )
                                        );
                                        return 0;
                                    }

                                    Vec3 sourcePos = context.getSource().getPosition();

                                    BlockPos pos = BlockPos.containing(
                                            world.x().get(sourcePos.x),
                                            world.y().get(sourcePos.y),
                                            world.z().get(sourcePos.z)
                                    );

                                    Constants.ProbingPos = pos;
                                    context.getSource().sendFeedback(
                                            Component.literal(
                                                    "Probe position set to " + pos.toShortString()
                                            )
                                    );
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(ClientCommands.literal("duration")
                        .then(ClientCommands.argument("ticks", IntegerArgumentType.integer(0, 100 * 20)) // max 100s
                                .suggests((_, builder) -> {
                                    builder.suggest(20);
                                    builder.suggest(40);
                                    builder.suggest(60);
                                    builder.suggest(80);
                                    builder.suggest(100);
                                    builder.suggest(200);
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    int duration = IntegerArgumentType.getInteger(context, "ticks");
                                    Constants.TimeShown = duration;
                                    Constants.COLLECTOR.setSampleSize(duration);
                                    context.getSource().sendFeedback(Component.literal("Successfully set duration!"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(ClientCommands.literal("toggle")
                        .executes(_ -> {
                            Constants.OverlayShown = !Constants.OverlayShown;
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
