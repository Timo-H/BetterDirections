package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BMessage {
    public BlockPos waypoint;
    public String waypointName;

    public BMessage() {
    }

    public BMessage(String waypointName) {

        this.waypointName = waypointName;
    }

    public static void encode(BMessage message, PacketBuffer buffer) {

        buffer.writeString(message.waypointName);
    }

    public static BMessage decode(PacketBuffer buffer) {

        return new BMessage(buffer.readString());
    }

    public static void handle(BMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            WaypointHandler.addWaypoint(player, message.waypointName);
            player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
                System.out.println(h.getWaypoints());
            });
        });
        context.setPacketHandled(true);
    }
}
