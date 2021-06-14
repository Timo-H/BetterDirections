package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class SyncronizeWaypointMessage {

    public int[] waypoints;
    public String waypointNames;

    public SyncronizeWaypointMessage() {}

    public SyncronizeWaypointMessage(int waypoints[], String waypointNames) {
        this.waypoints = waypoints;
        this.waypointNames = waypointNames;
    }
    // Encoder for this packet
    public static void encode(SyncronizeWaypointMessage message, PacketBuffer buffer) {
        buffer.writeVarIntArray(message.waypoints);
        buffer.writeString(message.waypointNames);
    }
    // Decoder for this packet
    public static SyncronizeWaypointMessage decode(PacketBuffer buffer) {
        return new SyncronizeWaypointMessage(buffer.readVarIntArray(), buffer.readString());
    }
    // Action performed by this packet
    public static void handle(SyncronizeWaypointMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            PlayerEntity player = Minecraft.getInstance().player;
            player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {

                // if there are no waypoints syncronized yet, make a new Arraylist
                ArrayList<BlockPos> waypoints = new ArrayList<>();
                ArrayList<String> waypointsNames = new ArrayList<>();
                // copy the coordinates to the new array
                if (message.waypoints != null) {
                    for (int i = 0; i < message.waypoints.length; i += 3)
                        waypoints.add(new BlockPos(message.waypoints[i], message.waypoints[i + 1], message.waypoints[i + 2]));
                }
                // copy the names to the new array
                if (message.waypointNames != null) {
                    String[] serverWaypointsNames = message.waypointNames.replace("[", "")
                            .replace("]", "").split(", ");
                    waypointsNames.addAll(Arrays.asList(serverWaypointsNames));
                }
                // set the waypoints
                capability.setWaypoints(waypoints);
                capability.setWaypointsNames(waypointsNames);
            });
        });
    }
}
