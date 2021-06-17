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
import java.util.Arrays;

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
                    System.out.println(Arrays.toString(waypoint));
                    tag.putIntArray("waypoint" + i, waypoint);
                    tag.putString("waypointname" + i, instance.getWaypointsNames().get(i));
                tag.putInt("totalwaypoints", instance.getWaypoints().size());
                }
            }
            return tag;
        }

        @Override
        // read the NBT data when an object is loaded, and sets the data for it
        public void readNBT(Capability<IWaypoints> capability, IWaypoints instance, Direction side, INBT nbt) {
            ArrayList<BlockPos> waypoints = new ArrayList<>();
            ArrayList<String> waypointsNames = new ArrayList<>();

            CompoundNBT data = (CompoundNBT) nbt;
            int totalWaypoints = data.getInt("totalwaypoints");
            for (int i = 0; i < totalWaypoints; i++) {
                int[] waypoint = data.getIntArray("waypoint" + i);
                waypoints.add(new BlockPos(waypoint[0], waypoint[1], waypoint[2]));
                waypointsNames.add(data.getString("waypointname" + i));
            }

            instance.setWaypoints(waypoints);
            instance.setWaypointsNames(waypointsNames);
        }
    }
}
