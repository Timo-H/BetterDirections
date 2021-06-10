package com.spacialnightmare.betterdirections.events;

import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.BMessage;
import com.spacialnightmare.betterdirections.network.message.MMessage;
import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.CreateNodes;
import com.spacialnightmare.betterdirections.item.ModItems;
import com.spacialnightmare.betterdirections.setup.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;


public class ModEvents {
    private boolean VisibleNodes = false;

    // Activates each time the player right clicks
    @SubscribeEvent
    public void RightClick(PlayerInteractEvent.RightClickItem event) {
        if (Minecraft.getInstance().player.inventory.getCurrentItem().getItem().equals(ModItems.WAYWARD_COMPASS.get())) {
            Minecraft.getInstance().player.sendChatMessage("right clicked compass");
        }
    }

    // Activates each time a key is pressed and looks if it matches with one of the set keybindings
    @SubscribeEvent
    public void Keyhandler(InputEvent.KeyInputEvent event) throws Throwable {

        KeyBinding[] keyBindings = ClientProxy.keyBindings;

        // if V is pressed
        if (keyBindings[0].isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            assert player != null;
            World world = player.world;
            player.sendStatusMessage(new TranslationTextComponent("message.toggle_nodes"), true);
            VisibleNodes = !VisibleNodes;

            Chunk chunk = world.getChunk(player.chunkCoordX, player.chunkCoordZ);
            if (!world.isRemote) {
                chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(h -> {
                    ArrayList<BlockPos> nodes = h.getNodes();
                    for (BlockPos node : nodes) {
                        CreateNodes.ShowNode(new BlockPos(node.getX(), 75, node.getZ()), world, VisibleNodes);
                    }
                });
            }


        // if B is pressed
        } else if (keyBindings[1].isPressed()) {
            ModNetwork.CHANNEL.sendToServer(new BMessage(new BlockPos(128, 65, 0)));

        // if N is pressed
        } else if (keyBindings[2].isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            assert player != null;
            World world = player.world;
            // if chunk is not loaded, load the chunk
            Chunk chunk = world.getChunkAt(new BlockPos(128, 65, 0));

            // get the nodes from the chunk
            if (!world.isRemote) {
                chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(n -> {
                    System.out.println("Capability server found");
                    System.out.println(n.getNodes().get(0));
                });
            }


        // if M is pressed
        } else if (keyBindings[3].isPressed()) {
            ModNetwork.CHANNEL.sendToServer(new MMessage(event.getKey()));
        }
    }
}
