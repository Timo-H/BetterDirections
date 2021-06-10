package com.spacialnightmare.betterdirections.network;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.message.BMessage;
import com.spacialnightmare.betterdirections.network.message.MMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BetterDirections.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));


    public static void init() {
        CHANNEL.registerMessage(0, MMessage.class, MMessage::encode, MMessage::decode, MMessage::handle);
        CHANNEL.registerMessage(1, BMessage.class, BMessage::encode, BMessage::decode, BMessage::handle);
    }
}
