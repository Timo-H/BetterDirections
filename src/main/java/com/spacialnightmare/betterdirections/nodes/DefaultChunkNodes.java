package com.spacialnightmare.betterdirections.nodes;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

// set the default values and methods for the capability
public class DefaultChunkNodes implements IChunkNodes {

    private ArrayList<BlockPos> nodes;

    @Override
    public void setNodes(ArrayList<BlockPos> nodes) {
        this.nodes = nodes;
    }

    @Override
    public ArrayList<BlockPos> getNodes() {
        return nodes;
    }
}
