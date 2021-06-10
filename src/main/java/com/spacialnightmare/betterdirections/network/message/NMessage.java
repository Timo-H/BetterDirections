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

    public static void encode(NMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.key);
    }

    public static NMessage decode(PacketBuffer buffer) {
        return new NMessage(buffer.readInt());
    }

    public static void handle(NMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {



        });
        context.setPacketHandled(true);
    }
}
