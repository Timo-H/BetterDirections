package com.spacialnightmare.betterdirections.nodes;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

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
