package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class WaypointEventHandler {

    @SubscribeEvent
    public void AttachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            WaypointsProvider provider = new WaypointsProvider();
            event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "waypoints"), provider);
            event.addListener(provider::invalidate);
        }
    }
}
