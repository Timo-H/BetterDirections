package com.spacialnightmare.betterdirections.nodes;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

// this class handles all the events used for the ChunkNode Capability
public class NodeEventHandler {

    // Activates each time a chunk is created
    @SubscribeEvent
    public void AttachChunkCapability(AttachCapabilitiesEvent<Chunk> event) {
        ChunkNodesProvider provider = new ChunkNodesProvider();
        if (event.getCapabilities().isEmpty()) {
            // attach a capability to the chunk
            event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "nodes"), provider);
            // attach an addlistener to the chunk, it invalidates the data so it doesnt stay loaded if not needed
            event.addListener(provider::invalidate);
        }
    }

    // Activates each time a chunk is loaded and creates the nodes for that chunk if they are not yet created, or if
    // the Integer NODES_PER_CHUNK was changed in the config
    @SubscribeEvent
    public void Chunkload(ChunkEvent.Load event) {
        World world = null;
        Chunk chunk = null;
        // get the world and chunk from the event
        if (event.getWorld() != null && event.getChunk() != null) {
            world = (World) event.getWorld();
            chunk = (Chunk) event.getChunk();
        }

        assert world != null;
        // only run this if this is being run server-side
        if (!world.isRemote) {
            Chunk finalChunk = chunk;
            World finalWorld = world;
            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                // checks if nodes exist and/or the same, and if not, creates new ones
                if (NodeHandler.CheckExistingNodes(h.getNodes())) {
                    NodeHandler.CreateChunkNodes(finalChunk, finalWorld);
                }
            });
        }
    }
}
