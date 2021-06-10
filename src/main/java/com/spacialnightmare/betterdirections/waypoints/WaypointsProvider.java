package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaypointsProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultWaypoints waypoints = new DefaultWaypoints();
    private final LazyOptional<IWaypoints> waypointsOptional = LazyOptional.of(() -> waypoints);

    public void invalidate() { waypointsOptional.invalidate(); }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityWaypoints.WAYPOINTS_CAPABILITY) {
            return waypointsOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CapabilityWaypoints.WAYPOINTS_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityWaypoints.WAYPOINTS_CAPABILITY.writeNBT(waypoints, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityWaypoints.WAYPOINTS_CAPABILITY != null) {
            CapabilityWaypoints.WAYPOINTS_CAPABILITY.readNBT(waypoints, null, nbt);
        }
    }
}
