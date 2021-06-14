package com.spacialnightmare.betterdirections.setup;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.util.KeyBindsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BetterDirections.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy implements IProxy{

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // register the keybinds
        KeyBindsInit.register(event);
    }

    @Override
    public void init() {
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
