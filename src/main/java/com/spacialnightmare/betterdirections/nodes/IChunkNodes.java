package com.spacialnightmare.betterdirections.nodes;

import java.util.ArrayList;

public interface IChunkNodes {
    void setNodes(ArrayList<ArrayList<Integer>> nodes);

    ArrayList<ArrayList<Integer>> getNodes();
}
