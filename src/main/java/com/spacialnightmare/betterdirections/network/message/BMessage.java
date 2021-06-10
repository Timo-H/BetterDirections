package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BMessage {
    public BlockPos chunk;

    public BMessage() {
    }

    public BMessage(BlockPos chunk) {
        this.chunk = chunk;
    }

    public static void encode(BMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.chunk);
    }

    public static BMessage decode(PacketBuffer buffer) {
        return new BMessage(buffer.readBlockPos());
    }

    public static void handle(BMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            ServerPlayerEntity player = context.getSender();
            World world = player.getEntityWorld();
            Chunk chunk = world.getChunkAt(message.chunk);

            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                System.out.println(h.getNodes());
            });

        });
        context.setPacketHandled(true);
    }
}
