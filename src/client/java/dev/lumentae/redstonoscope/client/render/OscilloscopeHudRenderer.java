package dev.lumentae.redstonoscope.client.render;

import dev.lumentae.redstonoscope.client.Constants;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;

public class OscilloscopeHudRenderer implements HudElement {
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        if (!Constants.OverlayShown || Constants.ProbingPos.equals(Constants.DEFAULT_POS))
            return;

        Minecraft mc = Minecraft.getInstance();

        float guiScale = mc.getWindow().getGuiScale();
        float scale = 3f / guiScale;

        // Take up 1/3 of the screen width and height
        int sizeX = Math.round(graphics.guiWidth() / (4f * scale) * 1.075f);
        int sizeY = Math.round(graphics.guiHeight() / (4f * scale));

        var pose = graphics.pose();
        pose.pushMatrix();

        pose.translate(8 * scale, 8 * scale);
        pose.scale(scale, scale);

        drawPanel(graphics, 0, 0, sizeX, sizeY);

        pose.popMatrix();
    }

    private void drawPanel(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, 0xFF080808);

        // Main Area
        graphics.fill(x + 1, y + 1,  x + width - 1, y + height - 1, 0xFF141414);

        drawGraph(graphics, x + 2, y + 2, width, height - 8);
    }

    private void drawGraph(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        int maxLevel = 15;
        Font font = Minecraft.getInstance().font;

        int graphTop = y + 10;
        int graphBottom = y + height - 9;
        float stepsPerLevel = (float) (graphBottom - graphTop) / maxLevel;
        for (int i = 0; i <= maxLevel; i++) {
            // Check if step can be divided by 3, so 15, 12, 9, 6, 3 or 0
            if (i % 3 == 0) {
                int paddingValue = font.width(String.valueOf(i)) / 2;
                if (i >= 10)
                    paddingValue = 0;

                graphics.text(font, String.valueOf(i), x + 2 + paddingValue, (int) (y + stepsPerLevel * (maxLevel - i) + 7), 0xFFFFFFFF);
            }
        }

        // Seperator
        graphics.verticalLine(x + 17, y - 2, y + height + 5, 0xFF454545);

        int graphStart = x + 22;
        int graphEnd = x + width - 8;
        for (int row = 0; row < 6; row++)
        {
            graphics.horizontalLine(graphStart, graphEnd, (int) (y + stepsPerLevel * row * 3 + 10), 0xFF454545);
        }

        int[] samples = Constants.COLLECTOR.getLastSamples();
        float horizontalSteps = (float) (graphEnd - graphStart) / (samples.length - 1);
        for (int datapoint = 0; datapoint < Constants.TimeShown - 1; datapoint++) {
            int sample0 = samples[Constants.TimeShown - 1 - datapoint];
            int sample1 = samples[Constants.TimeShown - 2 - datapoint];

            int x0 = graphStart + Math.round(datapoint * horizontalSteps);
            int x1 = graphStart + Math.round((datapoint + 1) * horizontalSteps);

            int y0 = graphTop + Math.round((maxLevel - sample0) * stepsPerLevel);
            int y1 = graphTop + Math.round((maxLevel - sample1) * stepsPerLevel);

            drawWaveFormSegment(graphics, x0, y0, x1, y0);
            drawWaveFormSegment(graphics, x1, y0, x1, y1);
        }
    }

    private void drawWaveFormSegment(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1) {
        int color = 0xFFFF0000;

        graphics.fill(
                Math.min(x0, x1),
                y0,
                Math.max(x0, x1) + 1,
                y0 + 1,
                color
        );

        if (y0 != y1) {
            graphics.fill(
                    x1,
                    Math.min(y0, y1),
                    x1 + 1,
                    Math.max(y0, y1) + 1,
                    color
            );
        }
    }
}
