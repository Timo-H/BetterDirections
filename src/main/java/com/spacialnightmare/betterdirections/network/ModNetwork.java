package com.spacialnightmare.betterdirections.network;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {

    public static final String NETWORK_VERSION = "0.1.0";
    // Create a new Simple channel, provide a resource Location for that channel,
    // and sync the client en server Network versions.
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BetterDirections.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));


    public static void init() {
        // Register all the different Packets used in this Channel
        CHANNEL.registerMessage(0, ShowNodesMessage.class, ShowNodesMessage::encode, ShowNodesMessage::decode,
                ShowNodesMessage::handle);
        CHANNEL.registerMessage(1, SetWaypointMessage.class, SetWaypointMessage::encode, SetWaypointMessage::decode,
                SetWaypointMessage::handle);
        CHANNEL.registerMessage(3, RemoveWaypointMesage.class, RemoveWaypointMesage::encode, RemoveWaypointMesage::decode, RemoveWaypointMesage::handle);
        CHANNEL.registerMessage(4, SyncronizeWaypointMessage.class, SyncronizeWaypointMessage::encode,
                SyncronizeWaypointMessage::decode, SyncronizeWaypointMessage::handle);
    }
}
