package com.spacialnightmare.betterdirections.nodes;

import com.spacialnightmare.betterdirections.util.ConfigManager;
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
    // Inject the Capability IChunkNodes into this class
    @CapabilityInject(IChunkNodes.class)
    public static Capability<IChunkNodes> CHUNK_NODES_CAPABILITY = null;
    // Get the instance
    private static final ConfigManager CMI = ConfigManager.getInstance();
    // Register this Capability class
    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkNodes.class, new ChunkNodeStorage(), DefaultChunkNodes::new);
    }
    // This class handles the data storage (writing and reading)
    public static class ChunkNodeStorage implements Capability.IStorage<IChunkNodes> {

        @Nullable
        @Override
        // write the data as NBT data and return it
        public INBT writeNBT(Capability<IChunkNodes> capability, IChunkNodes instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            // check if the total nodes in the chunk is the same as the nodePerChunk in the Config
            if (instance.getNodes().size() == CMI.nodesPerChunk()) {
                for (int i = 0; i < CMI.nodesPerChunk(); i++) {
                    int[] node = {instance.getNodes().get(i).getX(), instance.getNodes().get(i).getY(),
                            instance.getNodes().get(i).getZ()};
                    // put the node into the tag
                    tag.putIntArray("node" + i, node);
                }
            }
            return tag;
        }

        @Override
        // read the NBT data when an object is loaded, and sets the data for it
        public void readNBT(Capability<IChunkNodes> capability, IChunkNodes instance, Direction side, INBT nbt) {
            CompoundNBT data = (CompoundNBT) nbt;
            // check if data read, is the same size as NodesPerChunk
            if (data.size() == CMI.nodesPerChunk()) {
                // if it is, read the data into an Arraylist
                ArrayList<BlockPos> nodes = new ArrayList<>();
                for (int i = 0; i < CMI.nodesPerChunk(); i++) {

                    int[] nodeArray = data.getIntArray("node" + i);
                    BlockPos node = new BlockPos(nodeArray[0], nodeArray[1], nodeArray[2]);
                    nodes.add(node);
                }
                // set the nodes
                instance.setNodes(nodes);
            } else {
                // if the size doesnt match, it means the NodesPerChunk Config has been changed
                // set the nodes to null, when chunks get loaded, they will create new nodes
                // based on the NodesPerChunk config
                instance.setNodes(null);
            }
        }
    }
}
