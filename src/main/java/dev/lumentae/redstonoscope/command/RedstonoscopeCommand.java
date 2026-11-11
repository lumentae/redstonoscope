package dev.lumentae.redstonoscope.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.lumentae.redstonoscope.Constants;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;

public class RedstonoscopeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal(Constants.MOD_ID)
                .then(Commands.literal("location")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(context -> {
                                    assert context.getSource().getEntity() != null;
                                    Constants.ProbingPos = BlockPosArgument.getBlockPos(context, "pos");
                                    context.getSource().sendSystemMessage(Component.literal("Successfully set probe position!"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("duration")
                        .then(Commands.argument("ticks", IntegerArgumentType.integer(0, 100 * 20)) // max 100s
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
                                    context.getSource().sendSystemMessage(Component.literal("Successfully set duration!"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("toggle")
                        .executes(_ -> {
                            Constants.OverlayShown = !Constants.OverlayShown;
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
