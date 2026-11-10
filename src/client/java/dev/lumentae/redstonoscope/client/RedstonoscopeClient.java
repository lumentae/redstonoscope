package dev.lumentae.redstonoscope.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

public class RedstonoscopeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //HudElementRegistry.addLast("redstonoscope", new RedstonoscopeHudElement());
    }
}
