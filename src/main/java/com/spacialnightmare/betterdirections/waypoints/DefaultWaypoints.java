package com.spacialnightmare.betterdirections.waypoints;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class DefaultWaypoints implements IWaypoints{

    private ArrayList<BlockPos> waypoints;
    private ArrayList<String> waypointNames;

    @Override
    public void setWaypoints(ArrayList<BlockPos> waypoints) { this.waypoints = waypoints; }

    @Override
    public void setWaypointsNames(ArrayList<String> waypointsNames) { this.waypointNames = waypointsNames; }

    @Override
    public ArrayList<BlockPos> getWaypoints() {
        return waypoints;
    }

    @Override
    public ArrayList<String> getWaypointsNames() { return waypointNames; }
}
