package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.SyncronizeWaypointMessage;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;

// this class has all the methods used by the Waypoint Capability
public class WaypointHandler {
    private static boolean isPathing;
    private static String isPathingTo;
    private static ArrayList<BlockPos> path;
    private static boolean visibleWaypoints;

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
    public static void showWaypoints(PlayerEntity player) {
        World world = player.getEntityWorld();
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
            // get current waypoints
            ArrayList<BlockPos> waypoints = h.getWaypoints();

            // set the BlockPos to Gold Blocks
            for (BlockPos waypoint : waypoints) {
                NodeHandler.ShowNode(waypoint.up(3), world, isVisibleWaypoints(), Blocks.GOLD_BLOCK.getDefaultState());
            }
        });
    }

    // Synchronize player Waypoints
    public static void synchronizePlayerWaypoints(PacketDistributor.PacketTarget target, ServerPlayerEntity player) {
        player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {
            System.out.println(capability.getWaypointsNames());
            if (capability.getWaypoints() != null) {
                System.out.println("Old Capability: " + capability.getWaypointsNames());
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

    public static boolean isVisibleWaypoints() { return visibleWaypoints; }

    public static void setVisibleWaypoints(boolean visibleWaypoints) {
        WaypointHandler.visibleWaypoints = visibleWaypoints;
    }
}
