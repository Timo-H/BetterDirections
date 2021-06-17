package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import com.spacialnightmare.betterdirections.pathfinding.AStarPathfinding;
import com.spacialnightmare.betterdirections.pathfinding.Node;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TogglePathMessage {

    public boolean visible;

    public TogglePathMessage() {
    }

    public TogglePathMessage(boolean visible) { this.visible = visible; }

    // Encoder for this packet
    public static void encode(TogglePathMessage message, PacketBuffer buffer) { buffer.writeBoolean(message.visible); }

    // Decoder for this packet
    public static TogglePathMessage decode(PacketBuffer buffer) {
        return new TogglePathMessage(buffer.readBoolean());
    }

    // Action performed by this packet
    public static void handle(TogglePathMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // get world
            World world = context.getSender().getEntityWorld();
        });
    }
}
