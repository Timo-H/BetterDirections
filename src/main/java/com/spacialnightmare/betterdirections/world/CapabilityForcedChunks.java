package com.spacialnightmare.betterdirections.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityForcedChunks {

    @CapabilityInject(IForcedChunks.class)
    public static Capability<IForcedChunks> FORCED_CHUNKS = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IForcedChunks.class, new Storage(), DefaultForcedChunks::new);
    }

    public static class Storage implements Capability.IStorage<IForcedChunks> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IForcedChunks> capability, IForcedChunks instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putIntArray("Chunks", instance.getForcedChunks());
            return tag;
        }

        @Override
        public void readNBT(Capability<IForcedChunks> capability, IForcedChunks instance, Direction side, INBT nbt) {
            int[] chunks = ((CompoundNBT) nbt).getIntArray("Chunks");
            instance.setChunks(chunks);
        }
    }
}
