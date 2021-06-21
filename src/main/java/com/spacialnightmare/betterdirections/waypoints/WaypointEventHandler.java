package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.TogglePathMessage;
import com.spacialnightmare.betterdirections.pathfinding.AStarPathfinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

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
    // Synchronize waypoints on player login
    @SubscribeEvent
    public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
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
    // When the world gets closed, check if there is a path active and/or visible, if so, remove/toggle them
    @SubscribeEvent
    public void OnGameCloseEvent(WorldEvent.Save event) {
        // check if there is a path active
        if (WaypointHandler.isPathing()) {
            // check if the active path is visible
            if (!AStarPathfinding.isVISIBLE()) {
                // if it is visible, toggle it
                ModNetwork.CHANNEL.sendToServer(new TogglePathMessage(false));
            }
            // delete the current path destination
            WaypointHandler.setPathing(false);
            WaypointHandler.setIsPathingTo("");
        }
    }
}
