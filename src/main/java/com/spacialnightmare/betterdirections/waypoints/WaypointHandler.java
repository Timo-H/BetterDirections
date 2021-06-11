package com.spacialnightmare.betterdirections.waypoints;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class WaypointHandler {
    public static void addWaypoint(ServerPlayerEntity player, String waypointName) {
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {

            ArrayList<BlockPos> waypoints = h.getWaypoints();
            ArrayList<String> waypointsNames = h.getWaypointsNames();

            if (waypoints == null) {
                ArrayList<BlockPos> newWaypoints = new ArrayList<>();
                newWaypoints.add(player.getPosition());
                h.setWaypoints(newWaypoints);

                ArrayList<String> newWaypointsNames = new ArrayList<>();
                newWaypointsNames.add(waypointName);
                h.setWaypointsNames(newWaypointsNames);
            } else {
                waypoints.add(player.getPosition());
                waypointsNames.add(waypointName);
            }
        });
    }

    public static void removeWaypoint(ServerPlayerEntity player, String waypointName) {
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {

            ArrayList<BlockPos> waypoints = h.getWaypoints();
            ArrayList<String> waypointsNames = h.getWaypointsNames();

            waypoints.remove(waypointsNames.indexOf(waypointName));
            waypointsNames.remove(waypointName);
        });
    }

    public static void showWaypoint(ServerPlayerEntity player, String waypoint) {
        World world = player.getEntityWorld();
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {

            ArrayList<BlockPos> waypoints = h.getWaypoints();
            ArrayList<String> waypointsNames = h.getWaypointsNames();

            world.setBlockState(waypoints.get(waypointsNames.indexOf(waypoint)), Blocks.GOLD_BLOCK.getDefaultState());
        });

    }
}
