package com.spacialnightmare.betterdirections.network.message;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NMessage {
    public int key;

    public NMessage() {
    }

    public NMessage(int key) {
        this.key = key;
    }

    public static void encode(MMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.key);
    }

    public static MMessage decode(PacketBuffer buffer) {
        return new MMessage(buffer.readInt());
    }

    public static void handle(MMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {



        });
        context.setPacketHandled(true);
    }
}
