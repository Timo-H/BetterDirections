package com.spacialnightmare.betterdirections.nodes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkNodesProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultChunkNodes nodes = new DefaultChunkNodes();
    private final LazyOptional<IChunkNodes> nodesOptional = LazyOptional.of(() -> nodes);
    // invalidate the LazyOptional
    public void invalidate() {nodesOptional.invalidate();}

    @Nonnull
    @Override
    // return the LazyOptional if the capability thats being called is this one, otherwise return an empty LazyOptional
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityChunkNodes.CHUNK_NODES_CAPABILITY) {
            return nodesOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    // Serialize the NBT data
    public CompoundNBT serializeNBT() {
        if (CapabilityChunkNodes.CHUNK_NODES_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityChunkNodes.CHUNK_NODES_CAPABILITY.writeNBT(nodes, null);
        }
    }

    @Override
    // Deserialize the NBT data
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityChunkNodes.CHUNK_NODES_CAPABILITY != null) {
            CapabilityChunkNodes.CHUNK_NODES_CAPABILITY.readNBT(nodes, null, nbt);
        }
    }
}
