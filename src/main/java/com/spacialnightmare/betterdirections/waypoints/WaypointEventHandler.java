package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

// this class handles all the events used for the Waypoint Capability
public class WaypointEventHandler {

    // Activates each time a Entity is created
    @SubscribeEvent
    public void AttachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            System.out.println("Attaching capability");
            WaypointsProvider provider = new WaypointsProvider();
            // attach a capability to the Entity if it is a player
            event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "waypoints"), provider);
            // attach an addlistener to the player, it invalidates the data so it doesnt stay loaded if not needed
            event.addListener(provider::invalidate);
        }
    }
    // Synchronize waypoints on player login
    @SubscribeEvent
    public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        System.out.println("Player logged in!");
        IWaypoints capability = event.getPlayer().getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY)
                .orElseThrow(() -> new IllegalArgumentException("at login"));
        System.out.println(capability.getWaypointsNames());
        WaypointHandler.synchronizePlayerWaypoints(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                (ServerPlayerEntity) event.getPlayer());
    }
    // copy the capability when a playerEntity is cloned (Death, Going from End to overworld)
    @SubscribeEvent
    public void PlayerCloneEvent(PlayerEvent.Clone event) {
        // get old waypoints capability
        IWaypoints oldWaypoints = event.getOriginal().getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY)
                .orElseThrow(() -> new IllegalArgumentException("at cloning"));
        // get new waypoints capability
        IWaypoints waypoints = event.getPlayer().getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY)
                .orElseThrow(() -> new IllegalArgumentException("at pasting"));
        // copy the old capability to the new one
        waypoints.setWaypoints(oldWaypoints.getWaypoints());
        waypoints.setWaypointsNames(oldWaypoints.getWaypointsNames());
        // synchronize waypoints to the client
        WaypointHandler.synchronizePlayerWaypoints(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                (ServerPlayerEntity) event.getPlayer());
    }

    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if (WaypointHandler.isVisibleWaypoints()) {
        }
    }
}
