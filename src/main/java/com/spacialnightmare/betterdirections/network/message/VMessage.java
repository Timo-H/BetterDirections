package com.spacialnightmare.betterdirections.network.message;


import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.CreateNodes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class VMessage {
    public Boolean visible;

    public VMessage() {
    }

    public VMessage(boolean visible) {
        this.visible = visible;
    }

    public static void encode(VMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.visible);
    }

    public static VMessage decode(PacketBuffer buffer) {
        return new VMessage(buffer.readBoolean());
    }

    public static void handle(VMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            ServerPlayerEntity player = context.getSender();
            World world = player.getEntityWorld();
            Chunk chunk = world.getChunkAt(player.getPosition());

            if (message.visible) {
                BlockPos blockPos = player.getPosition();
            } else {

            }

            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                ArrayList<BlockPos> nodes = h.getNodes();
                for (BlockPos node : nodes) {
                    CreateNodes.ShowNode(new BlockPos(node.getX(), 75, node.getZ()), world, message.visible);
                }
            });

        });
        context.setPacketHandled(true);
    }
}
