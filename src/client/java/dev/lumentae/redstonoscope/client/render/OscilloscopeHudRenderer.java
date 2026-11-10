package dev.lumentae.redstonoscope.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;

public class OscilloscopeHudRenderer implements HudElement {
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        float guiScale = mc.getWindow().getGuiScale();
        float scale = 3f / guiScale;

        // Take up 1/3 of the screen width and height
        int sizeX = Math.round(graphics.guiWidth() / (4f * scale));
        int sizeY = Math.round(graphics.guiHeight() / (4f * scale));

        // Put it to the top left corner of the screen
        int posX = sizeX / 25;
        int posY = sizeY / 25;

        var pose = graphics.pose();
        pose.pushMatrix();

        pose.translate(8 * scale, 8 * scale);
        pose.scale(scale, scale);

        drawPanel(graphics, posX, posY, sizeX, sizeY);

        pose.popMatrix();
    }

    private void drawPanel(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, width, height, 0xFF080808);

        // Main Area
        graphics.fill(x + 1, y + 1, width - 1, height - 1, 0xFF141414);

        drawGraph(graphics, x + 2, y + 2, width - 2, height - 2);
    }

    private void drawGraph(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        int maxLevel = 15;
        Font font = Minecraft.getInstance().font;

        int stepsPerLevel = height / maxLevel;
        for (int i = 0; i <= maxLevel; i++) {
            // Check if step can be divided by 3, so 15, 12, 9, 6, 3 or 0
            if (i % 3 == 0) {
                int paddingValue = font.width(String.valueOf(i)) / 2;
                if (i >= 10)
                    paddingValue = 0;

                graphics.text(font, String.valueOf(i), x + 2 + paddingValue, y + (stepsPerLevel - 1) * (15 - i) + 4, 0xFFFFFFFF);
            }
        }

        // Seperator
        graphics.verticalLine(x + 17, y - 2, y + height - 5, 0xFF454545);
    }
}
