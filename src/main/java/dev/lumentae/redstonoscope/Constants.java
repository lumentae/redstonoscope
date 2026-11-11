package dev.lumentae.redstonoscope;

import dev.lumentae.redstonoscope.collector.SampleCollector;
import net.minecraft.core.BlockPos;

public class Constants {
    public static final int MAX_REDSTONE_VALUE = 15;
    public static final int MIN_REDSTONE_VALUE = 0;
    public static final String MOD_ID = "redstonoscope";
    public static final SampleCollector COLLECTOR = new SampleCollector();
    public static final BlockPos DEFAULT_POS = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static BlockPos ProbingPos = DEFAULT_POS;
    public static int TimeShown = 10 * 20; // 200 ticks = 10s
}
