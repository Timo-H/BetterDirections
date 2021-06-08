package com.spacialnightmare.betterdirections.nodes;

import java.util.ArrayList;

public class DefaultChunkNodes implements IChunkNodes {

    private ArrayList<ArrayList<Integer>> nodes;

    @Override
    public void setNodes(ArrayList<ArrayList<Integer>> nodes) {
        this.nodes = nodes;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getNodes() {
        return nodes;
    }
}
