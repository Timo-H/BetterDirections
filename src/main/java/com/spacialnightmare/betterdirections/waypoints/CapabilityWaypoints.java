package com.spacialnightmare.betterdirections.waypoints;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CapabilityWaypoints {
    // Inject the Capability IWaypoints into this class
    @CapabilityInject(IWaypoints.class)
    public static Capability<IWaypoints> WAYPOINTS_CAPABILITY = null;
    // Register this Capability class
    public static void register() {
        CapabilityManager.INSTANCE.register(IWaypoints.class, new WaypointsStorage(), DefaultWaypoints::new);
    }
    // This class handles the data storage (writing and reading)
    public static class WaypointsStorage implements Capability.IStorage<IWaypoints> {

        @Nullable
        @Override
        // write the data as NBT data and return it
        public INBT writeNBT(Capability<IWaypoints> capability, IWaypoints instance, Direction side) {

            CompoundNBT tag = new CompoundNBT();
            if (instance.getWaypoints() != null && instance.getWaypointsNames() != null) {

                for (int i = 0; i < instance.getWaypoints().size(); i++) {

                    int[] waypoint = {instance.getWaypoints().get(i).getX(), instance.getWaypoints().get(i).getY(),
                            instance.getWaypoints().get(i).getZ()};
                    tag.putIntArray(instance.getWaypointsNames().get(i), waypoint);
                }
            }
            return tag;
        }

        @Override
        // read the NBT data when an object is loaded, and sets the data for it
        public void readNBT(Capability<IWaypoints> capability, IWaypoints instance, Direction side, INBT nbt) {

            if (instance.getWaypoints() == null) {
                instance.setWaypoints(instance.getWaypoints());

            } else {

                ArrayList<BlockPos> waypoints = new ArrayList<>();

                for (int i = 0; i < instance.getWaypoints().size(); i++) {

                    int[] waypointsArray = ((CompoundNBT) nbt).getIntArray(instance.getWaypointsNames().get(i));
                    BlockPos waypoint = new BlockPos(waypointsArray[0], waypointsArray[1], waypointsArray[2]);
                    waypoints.add(waypoint);
                }
                instance.setWaypoints(waypoints);
            }
            instance.setWaypointsNames(instance.getWaypointsNames());
        }
    }
}
