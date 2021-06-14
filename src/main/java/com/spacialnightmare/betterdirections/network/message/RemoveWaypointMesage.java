package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import com.spacialnightmare.betterdirections.waypoints.WaypointSynchronisation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveWaypointMesage {

    public String waypointName;

    public RemoveWaypointMesage() {
    }

    public RemoveWaypointMesage(String waypointName) {
        this.waypointName = waypointName;
    }
    // Encoder for this packet
    public static void encode(RemoveWaypointMesage message, PacketBuffer buffer) {
        buffer.writeString(message.waypointName);
    }
    // Decoder for this packet
    public static RemoveWaypointMesage decode(PacketBuffer buffer) {
        return new RemoveWaypointMesage(buffer.readString());
    }
    // Action performed by this packet
    public static void handle(RemoveWaypointMesage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            WaypointHandler.removeWaypoint(player, message.waypointName);
            // Synchronize to the client
            WaypointSynchronisation.Synchronize(player, context);
        });
        context.setPacketHandled(true);
    }
}
