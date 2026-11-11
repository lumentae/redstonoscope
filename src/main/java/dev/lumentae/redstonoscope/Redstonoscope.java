package dev.lumentae.redstonoscope;

import dev.lumentae.redstonoscope.command.RedstonoscopeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Redstonoscope implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(RedstonoscopeCommand::register);
    }
}
