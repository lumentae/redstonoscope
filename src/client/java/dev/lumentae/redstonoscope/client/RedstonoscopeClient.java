package dev.lumentae.redstonoscope.client;

import dev.lumentae.redstonoscope.Constants;
import dev.lumentae.redstonoscope.client.render.OscilloscopeHudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.RedstoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public class RedstonoscopeClient implements ClientModInitializer {
    public static BlockPos ProbingPos = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static int TimeShown = 10 * 20; // 200 ticks = 10s

    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(Identifier.parse("redstonoscope"), new OscilloscopeHudRenderer());
        ClientTickEvents.END_CLIENT_TICK.register((minecraft) -> {
            if (minecraft.level == null) {
                return;
            }

            if (ProbingPos.getX() == Integer.MAX_VALUE ||
                ProbingPos.getY() == Integer.MAX_VALUE ||
                ProbingPos.getZ() == Integer.MAX_VALUE) {
                return;
            }

            BlockState blockState = minecraft.level.getBlockState(ProbingPos);
            int power = blockState.getValue(RedstoneWireBlock.POWER);

            Constants.COLLECTOR.addSample(power);
        });
    }
}
