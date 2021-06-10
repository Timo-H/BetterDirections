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
    public BlockPos waypoint;

    public BMessage() {
    }

    public BMessage(BlockPos waypoint) {
        this.waypoint = waypoint;
    }

    public static void encode(BMessage message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.waypoint);
    }

    public static BMessage decode(PacketBuffer buffer) {
        return new BMessage(buffer.readBlockPos());
    }

    public static void handle(BMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {



        });
        context.setPacketHandled(true);
    }
}
