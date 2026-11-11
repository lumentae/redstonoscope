package dev.lumentae.redstonoscope.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.lumentae.redstonoscope.Constants;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;

public class RedstonoscopeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal(Constants.MOD_ID)
                        .then(Commands.argument("action", StringArgumentType.word())
                                .then(Commands.argument("location", BlockPosArgument.blockPos())
                                        .executes(context -> {
                                            assert context.getSource().getEntity() != null;
                                            Constants.ProbingPos = context.getSource().getEntity().blockPosition();
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                                .suggests((context, builder) -> {
                                    builder.suggest("location");
                                    builder.suggest("duration");
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String action = context.getArgument("action", String.class);
                                    if (action.equals("duration")) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    context.getSource().sendFailure(Component.literal("Invalid subcommand!"));
                                    return 0;
                                })
                        )
                .executes(context -> {
                    context.getSource().sendFailure(Component.literal("Use a subcommand!"));
                    return 0;
                })
        );
    }
}
