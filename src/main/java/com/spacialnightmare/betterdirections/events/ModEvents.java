package com.spacialnightmare.betterdirections.events;

import com.spacialnightmare.betterdirections.item.ModItems;
import com.spacialnightmare.betterdirections.setup.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {

    // Activates each time a chunk is loaded (for now it just gets the chunk coords)
    @SubscribeEvent
    public void placeNodes(ChunkEvent.Load event) {
        IChunk chunk = event.getChunk();
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendChatMessage("Chunk Loaded: " + chunk.getPos());
        }
    }

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
}
