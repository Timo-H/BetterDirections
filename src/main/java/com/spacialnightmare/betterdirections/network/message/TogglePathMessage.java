package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.block.ModBlocks;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import com.spacialnightmare.betterdirections.pathfinding.AStarPathfinding;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
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
            if (WaypointHandler.getPath() != null) {
                ArrayList<BlockPos> path = WaypointHandler.getPath();
                // for every node in the path, set/remove a waypoint lantern 2 blocks up
                AStarPathfinding.setVISIBLE(message.visible);
                for (BlockPos node : path) {
                    NodeHandler.ShowNode(node.up(6), world, message.visible, ModBlocks.WAYPOINT_LANTERN.get().getDefaultState());
                }
            } else {
                context.getSender().sendStatusMessage(new TranslationTextComponent("message.no_path"),
                        true);;
            }
        });
    }
}
