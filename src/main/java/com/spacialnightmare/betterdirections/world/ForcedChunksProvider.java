package com.spacialnightmare.betterdirections.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ForcedChunksProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultForcedChunks chunks = new DefaultForcedChunks();
    private final LazyOptional<IForcedChunks> forcedChunksOptional = LazyOptional.of(() -> chunks);

    public void invalidate() { forcedChunksOptional.invalidate(); }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return forcedChunksOptional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CapabilityForcedChunks.FORCED_CHUNKS == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityForcedChunks.FORCED_CHUNKS.writeNBT(chunks, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityForcedChunks.FORCED_CHUNKS != null) {
            CapabilityForcedChunks.FORCED_CHUNKS.readNBT(chunks, null, nbt);
        }
    }
}
