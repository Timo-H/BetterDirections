package com.spacialnightmare.betterdirections.data;

import com.spacialnightmare.betterdirections.util.Config;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityChunkNodes {

    @CapabilityInject(IChunkNodes.class)
    public static Capability<IChunkNodes> CHUNK_NODES_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkNodes.class, new Storage(), DefaultChunkNodes::new);
    }

    public static class Storage implements Capability.IStorage<IChunkNodes> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IChunkNodes> capability, IChunkNodes instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            if (Config.NODES_PER_CHUNK.get() == 256) {
                for (int i = 0; i < 16; i++) {
                    tag.putInt("node" + i + "X", instance.getNodes().get(i).get(0));
                    tag.putInt("node" + i + "Y", instance.getNodes().get(i).get(1));
                    tag.putInt("node" + i + "Z", instance.getNodes().get(i).get(2));
                }
            }
            return tag;
        }

        @Override
        public void readNBT(Capability<IChunkNodes> capability, IChunkNodes instance, Direction side, INBT nbt) {

        }
    }
}
