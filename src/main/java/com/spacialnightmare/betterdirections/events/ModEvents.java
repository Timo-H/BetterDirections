package com.spacialnightmare.betterdirections.events;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {

    @SubscribeEvent
    public void placeNodes(ChunkEvent.Load event) {
        IChunk chunk = event.getChunk();
        ChunkPos pos = chunk.getPos();
        System.out.println(pos.getXStart() + ", " + pos.getXEnd() + ", " + pos.getZStart() + ", " + pos.getZEnd() +
                ", " + pos + ", " + pos.x + ", " + pos.z);
    }
}
