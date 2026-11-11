package dev.lumentae.redstonoscope.client;

import dev.lumentae.redstonoscope.Constants;
import dev.lumentae.redstonoscope.client.render.OscilloscopeHudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.RedstoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RedstonoscopeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(Identifier.parse(Constants.MOD_ID), new OscilloscopeHudRenderer());
        ClientTickEvents.END_CLIENT_TICK.register((minecraft) -> {
            if (minecraft.level == null) {
                return;
            }

            if (Constants.ProbingPos.getX() == Integer.MAX_VALUE ||
                Constants.ProbingPos.getY() == Integer.MAX_VALUE ||
                Constants.ProbingPos.getZ() == Integer.MAX_VALUE) {
                return;
            }

            BlockState blockState = minecraft.level.getBlockState(Constants.ProbingPos);
            int power = blockState.getValue(RedstoneWireBlock.POWER);

            Constants.COLLECTOR.addSample(power);
        });
    }
}
