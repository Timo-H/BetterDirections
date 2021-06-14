package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AskWaypointMessage {

    public AskWaypointMessage() { }

    public static void encode(AskWaypointMessage message, PacketBuffer buffer) { }

    public static AskWaypointMessage decode(PacketBuffer buffer) { return new AskWaypointMessage(); }

    public static void handle(AskWaypointMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            ServerPlayerEntity player = context.getSender();
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
