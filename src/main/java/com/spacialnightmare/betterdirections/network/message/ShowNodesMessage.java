package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
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

    public static void encode(ShowNodesMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.visible);
    }

    public static ShowNodesMessage decode(PacketBuffer buffer) {
        return new ShowNodesMessage(buffer.readBoolean());
    }

    public static void handle(ShowNodesMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            ServerPlayerEntity player = context.getSender();
            World world = player.getEntityWorld();

            ArrayList<Chunk> chunks = new ArrayList<>();

            if (message.visible) {
                // if toggle is true, then set the chunk that will be in the middle
                NodeHandler.setMidChunk(world.getChunkAt(player.getPosition()));
            }
            // add chunks in a 11 x 11 around the player into the list
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
            // for every chunk in the list, make the nodes visible using gold blocks
            for (Chunk chunk : chunks) {
                chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                    ArrayList<BlockPos> nodes = h.getNodes();
                    for (BlockPos node : nodes) {
                        NodeHandler.ShowNode(new BlockPos(node.getX(), 75, node.getZ()), world, message.visible);
                    }
                });
            }

        });
        context.setPacketHandled(true);
    }
}
