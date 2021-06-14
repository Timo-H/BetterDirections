package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SyncronizeWaypointMessage {

    public BlockPos waypoint;
    public String waypointName;
    public int waypointIndex;

    public SyncronizeWaypointMessage() {}

    public SyncronizeWaypointMessage(BlockPos waypoint, String waypointName, int waypointIndex) {
        this.waypoint = waypoint;
        this.waypointName = waypointName;
        this.waypointIndex = waypointIndex;
    }

    public static void encode(SyncronizeWaypointMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.waypoint);
        buffer.writeString(message.waypointName);
        buffer.writeInt(message.waypointIndex);
    }

    public static SyncronizeWaypointMessage decode(PacketBuffer buffer) {
        return new SyncronizeWaypointMessage(buffer.readBlockPos(), buffer.readString(), buffer.readInt());
    }

    public static void handle(SyncronizeWaypointMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            PlayerEntity player = Minecraft.getInstance().player;
            player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {

                // if there are no waypoints syncronized yet, make a new Arraylist
                ArrayList<BlockPos> waypoints;
                ArrayList<String> waypointsNames;
                if (capability.getWaypoints() == null) {
                    System.out.println("no waypoints found");
                    waypoints = new ArrayList<>();
                    waypointsNames = new ArrayList<>();
                } else {
                    waypoints = capability.getWaypoints();
                    waypointsNames = capability.getWaypointsNames();
                }

                // check if waypoint already exists
                System.out.println("Checking waypoints");
                try {
                    // if it does, then it checks if the values still match, and changes it if needed
                    if (waypoints.get(message.waypointIndex) != message.waypoint) {
                        waypoints.set(message.waypointIndex, message.waypoint);
                    }
                    if (waypointsNames.get(message.waypointIndex) != message.waypointName) {
                        waypointsNames.set(message.waypointIndex, message.waypointName);
                    }
                    // if it doesnt, then it just adds the waypoint
                } catch ( IndexOutOfBoundsException e ) {
                    waypoints.add(message.waypointIndex, message.waypoint);
                    waypointsNames.add(message.waypointIndex, message.waypointName);
                }
                // set the waypoints
                System.out.println("setting waypoints");
                capability.setWaypoints(waypoints);
                capability.setWaypointsNames(waypointsNames);
            });

        });
    }
}
