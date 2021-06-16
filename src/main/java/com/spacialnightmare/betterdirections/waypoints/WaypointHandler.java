package com.spacialnightmare.betterdirections.waypoints;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

// this class has all the methods used by the Waypoint Capability
public class WaypointHandler {
    private static boolean isPathing;
    private static String isPathingTo;
    private static ArrayList<BlockPos> path;

    // Add a waypoint
    public static void addWaypoint(ServerPlayerEntity player, String waypointName) {
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
            // get current waypoint and waypoint names
            ArrayList<BlockPos> waypoints = h.getWaypoints();
            ArrayList<String> waypointsNames = h.getWaypointsNames();

            // if there are no current waypoints, then make new arrays
            if (waypoints == null) {
                ArrayList<BlockPos> newWaypoints = new ArrayList<>();
                newWaypoints.add(player.getPosition());
                h.setWaypoints(newWaypoints);

                ArrayList<String> newWaypointsNames = new ArrayList<>();
                newWaypointsNames.add(waypointName);
                h.setWaypointsNames(newWaypointsNames);
            } else {
                // add new waypoint to the arraylist
                waypoints.add(player.getPosition());
                waypointsNames.add(waypointName);
                h.setWaypoints(waypoints);
                h.setWaypointsNames(waypointsNames);
            }
        });
    }
    // remove a waypoint
    public static void removeWaypoint(ServerPlayerEntity player, String waypointName) {
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
            // get current waypoint and waypoint names
            ArrayList<BlockPos> waypoints = h.getWaypoints();
            ArrayList<String> waypointsNames = h.getWaypointsNames();

            // remove the given waypoint
            waypoints.remove(waypointsNames.indexOf(waypointName));
            waypointsNames.remove(waypointName);

            // set the waypoints
            h.setWaypoints(waypoints);
            h.setWaypointsNames(waypointsNames);
        });
    }
    // show a waypoint location
    public static void showWaypoint(ServerPlayerEntity player, String waypoint) {
        World world = player.getEntityWorld();
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
            // get current waypoint and waypoint name
            ArrayList<BlockPos> waypoints = h.getWaypoints();
            ArrayList<String> waypointsNames = h.getWaypointsNames();

            // set the give BlockPos to a Gold Block
            world.setBlockState(waypoints.get(waypointsNames.indexOf(waypoint)), Blocks.GOLD_BLOCK.getDefaultState());
        });

    }
    // Getters and Setters for the current Path being tracked
    public static boolean isPathing() {
        return isPathing;
    }

    public static void setPathing(boolean pathing) {
        isPathing = pathing;
    }

    public static String getIsPathingTo() {
        return isPathingTo;
    }

    public static void setIsPathingTo(String isPathingTo) {
        WaypointHandler.isPathingTo = isPathingTo;
    }

    public static ArrayList<BlockPos> getPath() { return path; }

    public static void setPath(ArrayList<BlockPos> path) { WaypointHandler.path = path; }
}
