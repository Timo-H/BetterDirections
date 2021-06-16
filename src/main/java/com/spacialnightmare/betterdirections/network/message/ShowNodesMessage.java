package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ShowNodesMessage {
    public Boolean visible;

    public ShowNodesMessage() {
    }

    public ShowNodesMessage(boolean visible) {
        this.visible = visible;
    }
    // Encoder for this packet
    public static void encode(ShowNodesMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.visible);
    }
    // Decoder for this packet
    public static ShowNodesMessage decode(PacketBuffer buffer) {
        return new ShowNodesMessage(buffer.readBoolean());
    }
    // Action performed by this packet
    public static void handle(ShowNodesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            ServerPlayerEntity player = context.getSender();
            World world = player.getEntityWorld();
            // make an arraylist for chunks
            ArrayList<Chunk> chunks = new ArrayList<>();

            if (message.visible) {
                // if toggle is true, then set the chunk that will be in the middle
                NodeHandler.setMidChunk(world.getChunkAt(player.getPosition()));
            }
            // add chunks in a 11 x 11 around the player into the arraylist
            if (NodeHandler.getMidChunk() != null) {
                chunks.add(NodeHandler.getMidChunk());
                for (int x = NodeHandler.getMidChunk().getPos().getXStart() - 80; x < NodeHandler.getMidChunk()
                        .getPos().getXStart() + 96; x+=16) {
                    for (int z = NodeHandler.getMidChunk().getPos().getZStart() - 80; z < NodeHandler.getMidChunk()
                            .getPos().getZStart() + 96; z+=16) {
                        chunks.add(world.getChunkAt(new BlockPos(x, 0, z)));
                    }
                }
            }
            // for every chunk in the list
            for (Chunk chunk : chunks) {
                chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                    ArrayList<BlockPos> nodes = h.getNodes();
                    // for every node in a chunk
                    for (BlockPos node : nodes) {
                        // show the nodes using gold blocks
                        NodeHandler.ShowNode(new BlockPos(node.getX(), 90, node.getZ()), world, message.visible,
                                Blocks.GOLD_BLOCK.getDefaultState());
                        // if there is a path to a waypoint currently active
                        if (WaypointHandler.isPathing()) {
                            // and the node is part of that path
                            if (WaypointHandler.getPath() != null && WaypointHandler.getPath().contains(node)) {
                                // place a diamond block under the gold one to indicate that it is part of the path
                                NodeHandler.ShowNode(new BlockPos(node.getX(), 85, node.getZ()), world, message.visible,
                                        Blocks.REDSTONE_BLOCK.getDefaultState());
                            }
                        }
                    }
                });
            }

        });
        context.setPacketHandled(true);
    }
}
