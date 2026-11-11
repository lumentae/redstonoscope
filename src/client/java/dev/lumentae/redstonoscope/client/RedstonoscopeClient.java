package dev.lumentae.redstonoscope.client;

import dev.lumentae.redstonoscope.client.command.RedstonoscopeCommand;
import dev.lumentae.redstonoscope.client.render.OscilloscopeHudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RedstonoscopeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(Identifier.parse(Constants.MOD_ID), new OscilloscopeHudRenderer());
        ClientCommandRegistrationCallback.EVENT.register(RedstonoscopeCommand::register);
        ClientTickEvents.START_CLIENT_TICK.register((minecraft) -> {
            if (Constants.ProbingPos.getX() == Integer.MAX_VALUE ||
                    Constants.ProbingPos.getY() == Integer.MAX_VALUE ||
                    Constants.ProbingPos.getZ() == Integer.MAX_VALUE) {
                return;
            }

            if (minecraft.level == null) {
                return;
            }

            BlockState targetBlockState = minecraft.level.getBlockState(Constants.ProbingPos);
            BlockPos targetPos = Constants.ProbingPos;
            Direction direction = Direction.UP;
            if (targetBlockState.is(Blocks.COMPARATOR) || targetBlockState.is(Blocks.REPEATER)) {
                direction = targetBlockState.getValue(DiodeBlock.FACING);
                if (targetBlockState.is(Blocks.COMPARATOR))
                    direction = direction.getOpposite();
                targetPos = targetPos.relative(direction);
            }

            int power = minecraft.level.getSignal(targetPos, direction);
            if (targetBlockState.is(Blocks.REDSTONE_BLOCK) ||
                    targetBlockState.is(Blocks.REDSTONE_TORCH) ||
                    targetBlockState.is(Blocks.REDSTONE_WALL_TORCH))
                power = 15;

            Constants.COLLECTOR.addSample(power);
        });
    }
}
