package com.spacialnightmare.betterdirections.data;

import com.spacialnightmare.betterdirections.util.Config;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CreateNodes {
    // check if there are already nodes created in the chunk, and if so, checks if the amount of nodes is the same as
    // specified in the config
    public static boolean CheckExistingNodes(@Nullable ArrayList<ArrayList<Integer>> exNodes) {
        if (exNodes != null) {
            return exNodes.size() != Config.NODES_PER_CHUNK.get();
        } else {
            return true;
        }
    }

    // Create the nodes for the given chunk, the amount of nodes created depends on the NODES_PER_CHUNK Integer in the config
    public static void CreateChunkNodes(Chunk chunk, World world) {
        System.out.println("Creating nodes");
        int totalNodesRow = 0;
        int distanceBetweenNodes = 0;
        if (Config.NODES_PER_CHUNK.get() == 256) {
            totalNodesRow = 16;
        } else if (Config.NODES_PER_CHUNK.get() == 64) {
            totalNodesRow = 8;
            distanceBetweenNodes = 1;
        } else if (Config.NODES_PER_CHUNK.get() == 16) {
            totalNodesRow = 4;
            distanceBetweenNodes = 3;
        } else {
            throw new IllegalArgumentException("Integer NODES_PER_CHUNK not correctly assigned!");
        }
        ArrayList<ArrayList<Integer>> nodes = new ArrayList<>();
        System.out.println("Nodes per row: " + totalNodesRow);
        for (int i = 0; i < totalNodesRow; i += distanceBetweenNodes) {
            System.out.println("Creating row: " + i);
            for (int j = 0; j < totalNodesRow; j += distanceBetweenNodes) {
                System.out.println("Creating node " + j);
                int height = 65;
                System.out.println("Height is: " + height);
                ArrayList<Integer> node = new ArrayList<>();
                node.add(chunk.getPos().x + i);
                node.add(height);
                node.add(chunk.getPos().z + j);
                nodes.add(node);
                System.out.println("Node" + i + " " + j + " created");
            }
        }

        chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(n -> {
            n.setNodes(nodes);
        });
        System.out.println(nodes);
        System.out.println("Nodes set!");


    }
}
