package com.spacialnightmare.betterdirections.world;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForcedChunksEventHandler {

    @SubscribeEvent
    public static void AttachWorldCapability(AttachCapabilitiesEvent<World> event) {
        ForcedChunksProvider provider = new ForcedChunksProvider();
        event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "chunks"), provider);
        event.addListener(provider::invalidate);
    }
}
