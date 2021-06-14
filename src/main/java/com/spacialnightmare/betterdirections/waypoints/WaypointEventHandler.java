package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

// this class handles all the events used for the Waypoint Capability
public class WaypointEventHandler {

    // Activates each time a Entity is created
    @SubscribeEvent
    public void AttachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            WaypointsProvider provider = new WaypointsProvider();
            // attach a capability to the Entity if it is a player
            event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "waypoints"), provider);
            // attach an addlistener to the player, it invalidates the data so it doesnt stay loaded if not needed
            event.addListener(provider::invalidate);
        }
    }
}
