package com.spacialnightmare.betterdirections.nodes;

import com.spacialnightmare.betterdirections.util.Config;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class NodeHandler {

    public static Boolean NodeVisibility = true;

    public static Chunk midChunk;

    public static Boolean getNodeVisibility() {
        return NodeVisibility;
    }

    public static Chunk getMidChunk() {
        return midChunk;
    }

    public static void setNodeVisibility(Boolean nodeVisibility) {
        NodeVisibility = nodeVisibility;
    }

    public static void setMidChunk(Chunk midChunk) {
        NodeHandler.midChunk = midChunk;
    }

    // check if there are already nodes created in the chunk, and if so, checks if the amount of nodes is the same as
    // specified in the config
    public static boolean CheckExistingNodes(@Nullable ArrayList<BlockPos> exNodes) {
        if (exNodes == null) {
            return true;
        }
        return exNodes.size() != Config.NODES_PER_CHUNK.get();
    }

    // Create the nodes for the given chunk, the amount of nodes created depends on the NODES_PER_CHUNK Integer in the config
    public static void CreateChunkNodes(Chunk chunk, World world) {
        // Determine distance between nodes depending on the NODES_PER_CHUNK
        int distanceBetweenNodes = 0;
        if (Config.NODES_PER_CHUNK.get() == 256) {
            distanceBetweenNodes = 1;
        } else if (Config.NODES_PER_CHUNK.get() == 64) {
            distanceBetweenNodes = 2;
        } else if (Config.NODES_PER_CHUNK.get() == 16) {
            distanceBetweenNodes = 4;
        } else {
            throw new IllegalArgumentException("Integer NODES_PER_CHUNK not correctly assigned!");
        }
        ArrayList<BlockPos> nodes = new ArrayList<>();

        // creating rows of nodes
        for (int i = 0; i < 16; i += distanceBetweenNodes) {
            // creating nodes
            for (int j = 0; j < 16; j += distanceBetweenNodes) {

                int x = chunk.getPos().getXStart() + i;
                int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, chunk.getPos().getXStart() + i,
                        chunk.getPos().getZStart() + j);
                int z = chunk.getPos().getZStart() + j;

                BlockPos node = new BlockPos(x, y, z);
                nodes.add(node);
            }
        }
        saveNodes(chunk, world, nodes);
    }

    public static void saveNodes(Chunk chunk, World world, ArrayList<BlockPos> nodes) {
        // saving the nodes to the chunk
        if (!world.isRemote) {
            chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(n -> {
                n.setNodes(nodes);
                chunk.markDirty();
            });
        }
    }

    // showing the nodes as gold block at Y-75 (actual nodes are at ground level), if there is no block in the way.
    public static void ShowNode(BlockPos pos, World world, Boolean visible) {
        if (visible) {
            if (world.isAirBlock(pos)) {
                world.setBlockState(pos, Blocks.GOLD_BLOCK.getDefaultState());
            }
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}