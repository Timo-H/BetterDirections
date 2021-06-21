package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import com.spacialnightmare.betterdirections.waypoints.WaypointSynchronisation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetWaypointMessage {
    public String waypointName;

    public SetWaypointMessage() {
    }

    public SetWaypointMessage(String waypointName) {

        this.waypointName = waypointName;
    }
    // Encoder for this packet
    public static void encode(SetWaypointMessage message, PacketBuffer buffer) {

        buffer.writeString(message.waypointName);
    }
    // Decoder for this packet
    public static SetWaypointMessage decode(PacketBuffer buffer) {

        return new SetWaypointMessage(buffer.readString());
    }
    // Action performed by this packet
    public static void handle(SetWaypointMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // get the player
            ServerPlayerEntity player = context.getSender();
            // add the waypoint
            WaypointHandler.addWaypoint(player, message.waypointName);
            // synchronize to the client
            WaypointSynchronisation.Synchronize(player, context);
        });
        context.setPacketHandled(true);
    }
}
