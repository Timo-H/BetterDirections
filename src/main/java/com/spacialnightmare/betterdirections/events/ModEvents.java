package com.spacialnightmare.betterdirections.events;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.CreateNodes;
import com.spacialnightmare.betterdirections.item.ModItems;
import com.spacialnightmare.betterdirections.setup.ClientProxy;
import com.spacialnightmare.betterdirections.world.CapabilityForcedChunks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;


public class ModEvents {
    private boolean VisibleNodes = false;
    private boolean NodesVisible = false;

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
        PlayerEntity player = Minecraft.getInstance().player;
        World world = player.world;

        // if V is pressed
        if (keyBindings[0].isPressed()) {
            Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.toggle_nodes"), true);
            VisibleNodes = !VisibleNodes;

            Chunk chunk = world.getChunk(player.chunkCoordX, player.chunkCoordZ);
            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                for (ArrayList<Integer> node : h.getNodes()) {
                    System.out.println(node);
                    CreateNodes.ShowNode(new BlockPos(node.get(0), 75, node.get(2)), world, VisibleNodes);
                }
            });

        // if B is pressed
        } else if (keyBindings[1].isPressed()) {
            player.sendMessage(new TranslationTextComponent("Height: " + world.getHeight(Heightmap.Type.WORLD_SURFACE, player.getPosition().getX(),
                    player.getPosition().getZ())), player.getUniqueID());

        // if N is pressed
        } else if (keyBindings[2].isPressed()) {
            // if chunk is not loaded, load the chunk
            ((ServerWorld) world).forceChunk(128,0 , true);
            player.sendMessage(new TranslationTextComponent("Chunk Loaded"), player.getUniqueID());

            // get the nodes from the chunk
            player.sendMessage(new TranslationTextComponent("N is pressed"), player.getUniqueID());
            world.getChunkAt(new BlockPos(128, 65, 0)).getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(n -> {
                player.sendMessage(new TranslationTextComponent("Node 0: " + n.getNodes().get(0)),
                        player.getUniqueID());
                });

        // if M is pressed
        } else if (keyBindings[3]. isPressed()) {
            player.sendMessage(new TranslationTextComponent("M is pressed"), player.getUniqueID());
            ServerWorld serverWorld = (ServerWorld) world;
            if (serverWorld.isRemote) return;
        }
    }

    @SubscribeEvent
    public void init(final FMLCommonSetupEvent event) {
        CapabilityChunkNodes.register();
        CapabilityForcedChunks.register();
    }

}
