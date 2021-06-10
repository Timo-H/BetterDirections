package com.spacialnightmare.betterdirections.nodes;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NodeEventHandler {

    // Activates each time a chunk is created and attaches a capability to it
    @SubscribeEvent
    public void AttachChunkCapability(AttachCapabilitiesEvent<Chunk> event) {
        ChunkNodesProvider provider = new ChunkNodesProvider();
        if (event.getCapabilities().isEmpty()) {
            event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "nodes"), provider);
            event.addListener(provider::invalidate);
        }
    }

    // Activates each time a chunk is loaded and creates the nodes for that chunk if they are not yet created, or if
    // the Integer NODES_PER_CHUNK was changed in the config
    @SubscribeEvent
    public void Chunkload(ChunkEvent.Load event) {
        System.out.println("Chunk Loading");
        World world = null;
        Chunk chunk = null;
        if (event.getWorld() != null && event.getChunk() != null) {
            world = (World) event.getWorld();
            chunk = (Chunk) event.getChunk();
        }

        assert world != null;
        if (!world.isRemote) {
            System.out.println("ServerWorld");
            Chunk finalChunk = chunk;
            World finalWorld = world;
            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                if (CreateNodes.CheckExistingNodes(h.getNodes())) {
                    CreateNodes.CreateChunkNodes(finalChunk, finalWorld);
                }
            });
        }
    }
}
