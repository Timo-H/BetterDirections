package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.SyncronizeWaypointMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
        synchronizePlayerWaypoints(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                (ServerPlayerEntity) event.getPlayer());
    }
    // Synchronize waypoints on player respawn
    @SubscribeEvent
    public void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        synchronizePlayerWaypoints(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                (ServerPlayerEntity) event.getPlayer());
    }
    // Synchronize waypoints when a player changes dimensions
    @SubscribeEvent
    public void PlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        synchronizePlayerWaypoints(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                (ServerPlayerEntity) event.getPlayer());
    }
    // Synchronize player Waypoints
    public void synchronizePlayerWaypoints(PacketDistributor.PacketTarget target, ServerPlayerEntity player) {
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {
            if (capability.getWaypoints() != null) {
                // read the coordinates
                int[] waypoints = new int[3 * capability.getWaypoints().size()];
                int i = 0;
                for (BlockPos waypoint : capability.getWaypoints()) {
                    waypoints[i] = waypoint.getX();
                    waypoints[i + 1] = waypoint.getY();
                    waypoints[i + 2] = waypoint.getZ();
                    i += 3;
                }
                // send a packet to the client with the current data
                ModNetwork.CHANNEL.send(target, new SyncronizeWaypointMessage(waypoints, capability.getWaypointsNames()
                        .toString()));
            }
        });
        }
}
