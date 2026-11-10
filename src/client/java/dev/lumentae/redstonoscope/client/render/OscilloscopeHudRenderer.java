package dev.lumentae.redstonoscope.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;

public class OscilloscopeHudRenderer implements HudElement {
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        int posX = 0;
        int posY = 0;
        int sizeX = 0;
        int sizeY = 0;

        sizeX = graphics.guiWidth();
        sizeY = graphics.guiHeight();
    }
}
