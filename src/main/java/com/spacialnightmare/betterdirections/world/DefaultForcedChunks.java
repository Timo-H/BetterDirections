package com.spacialnightmare.betterdirections.world;


public class DefaultForcedChunks implements IForcedChunks {

    private int[] chunks;

    @Override
    public void setChunks(int[] chunks) {
        this.chunks = chunks;
    }

    @Override
    public int[] getForcedChunks() {
        return chunks;
    }
}
