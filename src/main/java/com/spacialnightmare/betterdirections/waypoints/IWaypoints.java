package com.spacialnightmare.betterdirections.waypoints;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public interface IWaypoints {
    void setWaypoints(ArrayList<BlockPos> waypoints);
    void setWaypointsNames(ArrayList<String> waypointsNames);

    ArrayList<BlockPos> getWaypoints();
    ArrayList<String> getWaypointsNames();
}
