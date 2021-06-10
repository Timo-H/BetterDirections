package com.spacialnightmare.betterdirections.nodes;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public interface IChunkNodes {
    void setNodes(ArrayList<BlockPos> nodes);

    ArrayList<BlockPos> getNodes();
}
