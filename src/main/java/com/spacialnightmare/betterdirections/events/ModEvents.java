package com.spacialnightmare.betterdirections.events;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.data.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.data.ChunkNodesProvider;
import com.spacialnightmare.betterdirections.data.CreateNodes;
import com.spacialnightmare.betterdirections.item.ModItems;
import com.spacialnightmare.betterdirections.setup.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


public class ModEvents {
    // Activates each time a chunk is created and attaches a capability to it
    @SubscribeEvent
    public void AttachCapability(AttachCapabilitiesEvent<Chunk> event) {
        ChunkNodesProvider provider = new ChunkNodesProvider();
        event.addCapability(new ResourceLocation(BetterDirections.MOD_ID, "nodes"), provider);
        event.addListener(provider::invalidate);
    }

    // Activates each time a chunk is loaded and creates the nodes for that chunk if they are not yet created, or if
    // the Integer NODES_PER_CHUNK was changed in the config
    @SubscribeEvent
    public void Chunkload(ChunkEvent.Load event) {
        System.out.println(event.getChunk().getPos());
            Chunk chunk = (Chunk) event.getChunk();
            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                if (CreateNodes.CheckExistingNodes(h.getNodes())) {
                    CreateNodes.CreateChunkNodes(chunk, (World) event.getWorld());
                }
            });
    }

    // Activates each time the player right clicks
    @SubscribeEvent
    public void RightClick(PlayerInteractEvent.RightClickItem event) {
        if (Minecraft.getInstance().player.inventory.getCurrentItem().getItem().equals(ModItems.WAYWARD_COMPASS.get())) {
            Minecraft.getInstance().player.sendChatMessage("right clicked compass");
        }
    }

    // Activates each time a key is pressed and looks if it matches with one of the set keybindings
    @SubscribeEvent
    public void Keyhandler(InputEvent.KeyInputEvent event) {
        KeyBinding[] keyBindings = ClientProxy.keyBindings;

        if (keyBindings[0].isPressed()) {
            System.out.println("B is pressed!");

        }
    }

    @SubscribeEvent
    public void init(final FMLCommonSetupEvent event) {
        CapabilityChunkNodes.register();
    }
}
