package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.SyncronizeWaypointMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class WaypointSynchronisation {

    public static void Synchronize(ServerPlayerEntity player, NetworkEvent.Context context) {
        // synchronize to the client
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {
            if (capability.getWaypoints() != null) {
                // read the coordinates
                int [] waypoints = new int[3*capability.getWaypoints().size()];
                int i = 0;
                for (BlockPos waypoint : capability.getWaypoints()) {
                    waypoints[i] = waypoint.getX();
                    waypoints[i+1] = waypoint.getY();
                    waypoints[i+2] = waypoint.getZ();
                    i += 3;
                }
                // reply to the client with a packet that holds current waypoint data
                ModNetwork.CHANNEL.reply(new SyncronizeWaypointMessage(waypoints,
                        capability.getWaypointsNames().toString()), context);
            }
        });
    }
}
