package dev.lumentae.redstonoscope.client;

import dev.lumentae.redstonoscope.client.collector.SampleCollector;
import net.minecraft.core.BlockPos;

public class Constants {
    public static final String MOD_ID = "redstonoscope";
    public static final SampleCollector COLLECTOR = new SampleCollector();
    public static final BlockPos DEFAULT_POS = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static BlockPos ProbingPos = DEFAULT_POS;
    public static int TimeShown = 10 * 20; // 200 ticks = 10s
    public static boolean OverlayShown = true;
}
