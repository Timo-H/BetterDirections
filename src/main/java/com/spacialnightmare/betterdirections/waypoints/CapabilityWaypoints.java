package com.spacialnightmare.betterdirections.waypoints;

import com.spacialnightmare.betterdirections.util.Config;
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

    @CapabilityInject(IWaypoints.class)
    public static Capability<IWaypoints> WAYPOINTS_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IWaypoints.class, new WaypointsStorage(), DefaultWaypoints::new);
    }

    public static class WaypointsStorage implements Capability.IStorage<IWaypoints> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IWaypoints> capability, IWaypoints instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            for (int i = 0; i < instance.getWaypoints().size(); i++) {
                int[] waypoint = {instance.getWaypoints().get(i).getX(), instance.getWaypoints().get(i).getY(),
                        instance.getWaypoints().get(i).getZ()};
                tag.putIntArray(instance.getWaypointsNames().get(i), waypoint);
            }
            return tag;
        }

        @Override
        public void readNBT(Capability<IWaypoints> capability, IWaypoints instance, Direction side, INBT nbt) {
            ArrayList<BlockPos> waypoints = new ArrayList<>();
            for (int i = 0; i < Config.NODES_PER_CHUNK.get(); i++) {

                int[] waypointsArray = ((CompoundNBT) nbt).getIntArray(instance.getWaypointsNames().get(i));
                BlockPos waypoint = new BlockPos(waypointsArray[0], waypointsArray[1], waypointsArray[2]);
                waypoints.add(waypoint);
            }
            instance.setWaypoints(waypoints);
        }
    }
}
