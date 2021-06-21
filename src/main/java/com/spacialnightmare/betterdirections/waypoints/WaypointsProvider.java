package com.spacialnightmare.betterdirections.waypoints;

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
    // invalidate the LazyOptional
    public void invalidate() { waypointsOptional.invalidate(); }


    @Nonnull
    @Override
    // return the LazyOptional if the capability thats being called is this one, otherwise return an empty LazyOptional
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityWaypoints.WAYPOINTS_CAPABILITY) {
            return waypointsOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    // Serialize the NBT data
    public CompoundNBT serializeNBT() {
        if (CapabilityWaypoints.WAYPOINTS_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityWaypoints.WAYPOINTS_CAPABILITY.writeNBT(waypoints, null);
        }
    }

    @Override
    // Deserialize the NBT data
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityWaypoints.WAYPOINTS_CAPABILITY != null) {
            CapabilityWaypoints.WAYPOINTS_CAPABILITY.readNBT(waypoints, null, nbt);
        }
    }
}
