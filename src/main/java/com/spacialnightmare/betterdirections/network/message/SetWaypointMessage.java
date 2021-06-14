package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetWaypointMessage {
    public BlockPos waypoint;
    public String waypointName;

    public SetWaypointMessage() {
    }

    public SetWaypointMessage(String waypointName) {

        this.waypointName = waypointName;
    }

    public static void encode(SetWaypointMessage message, PacketBuffer buffer) {

        buffer.writeString(message.waypointName);
    }

    public static SetWaypointMessage decode(PacketBuffer buffer) {

        return new SetWaypointMessage(buffer.readString());
    }

    public static void handle(SetWaypointMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            WaypointHandler.addWaypoint(player, message.waypointName);

            // synchronize to the client
            player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {
                if (capability.getWaypoints() != null) {
                    for (int i = 0; i < capability.getWaypoints().size(); i++) {
                        ModNetwork.CHANNEL.reply(new SyncronizeWaypointMessage(capability.getWaypoints().get(i),
                                capability.getWaypointsNames().get(i), i), context);
                    }
                }
            });
        });
        context.setPacketHandled(true);
    }
}
