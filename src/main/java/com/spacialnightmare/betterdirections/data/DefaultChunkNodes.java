package com.spacialnightmare.betterdirections.data;

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
