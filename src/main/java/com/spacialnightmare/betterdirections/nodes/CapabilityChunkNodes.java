package com.spacialnightmare.betterdirections.nodes;

import com.spacialnightmare.betterdirections.util.Config;
import com.sun.media.jfxmedia.logging.Logger;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.ArrayList;

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

            for (int i = 0; i < Config.NODES_PER_CHUNK.get(); i++) {
                int [] node = {instance.getNodes().get(i).getX(), instance.getNodes().get(i).getY(),
                        instance.getNodes().get(i).getZ()};
                tag.putIntArray("node" + i, node);
            }
            return tag;
        }

        @Override
        public void readNBT(Capability<IChunkNodes> capability, IChunkNodes instance, Direction side, INBT nbt) {
            ArrayList<BlockPos> nodes = new ArrayList<>();
            for (int i = 0; i < Config.NODES_PER_CHUNK.get(); i++) {

                int[] nodeArray = ((CompoundNBT) nbt).getIntArray("node" + i);
                BlockPos node = new BlockPos(nodeArray[0], nodeArray[1], nodeArray[2]);
                nodes.add(node);
            }
            instance.setNodes(nodes);
        }
    }
}
