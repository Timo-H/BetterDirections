package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import com.spacialnightmare.betterdirections.pathfinding.AStarPathfinding;
import com.spacialnightmare.betterdirections.pathfinding.Node;
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
                for (int x = chunk.getPos().getXStart(); x < chunk.getPos().getXStart() + 16; x++) {
                    for (int z = chunk.getPos().getZStart(); z < chunk.getPos().getZStart() + 16; z++) {
                        NodeHandler.ShowNode(new BlockPos(x, 108, z), world, message.visible,
                                Blocks.WHITE_WOOL.getDefaultState());
                    }
                }
                chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                    ArrayList<BlockPos> nodes = h.getNodes();
                    // for every node in a chunk
                    for (BlockPos pos : nodes) {
                        // show the nodes using gold blocks
                        NodeHandler.ShowNode(new BlockPos(pos.getX(), 109, pos.getZ()), world, message.visible,
                                Blocks.BLACK_WOOL.getDefaultState());
                        // if there is a path to a waypoint currently active
                        if (WaypointHandler.isPathing()) {
                            // and the node is part of that path
                            // toggle the pathing node indicators
                            for (Node node : AStarPathfinding.OPEN) {
                                AStarPathfinding.drawNode(node, Blocks.LIME_WOOL.getDefaultState(), world, message.visible);
                            }
                            for (Node node : AStarPathfinding.CLOSED) {
                                AStarPathfinding.drawNode(node, Blocks.RED_WOOL.getDefaultState(), world, message.visible);
                            }
                            for (BlockPos node : WaypointHandler.getPath()) {
                                BlockPos pathNode = new BlockPos(node.getX(), 111, node.getZ());
                                NodeHandler.ShowNode(pathNode, world, message.visible, Blocks.GOLD_BLOCK.getDefaultState());
                            }
                        }
                    }
                });
            }

        });
        context.setPacketHandled(true);
    }
}
