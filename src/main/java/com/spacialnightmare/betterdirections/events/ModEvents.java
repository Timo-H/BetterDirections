package com.spacialnightmare.betterdirections.events;

import com.spacialnightmare.betterdirections.setup.ClientProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {

    // Activates each time a chunk is loaded (for now it just gets the chunk coords)
    @SubscribeEvent
    public void placeNodes(ChunkEvent.Load event) {
        IChunk chunk = event.getChunk();
        ChunkPos pos = chunk.getPos();
    }

    // Activates each time a key is pressed and looks if it matches with one of the set keybindings
    @SubscribeEvent
    public void Keyhandler(InputEvent.KeyInputEvent event) {
        KeyBinding[] keyBindings = ClientProxy.keyBindings;

        if (keyBindings[0].isPressed()) {
            System.out.println("Key binding ="+keyBindings[0].getKeyDescription());
        }
    }
}
