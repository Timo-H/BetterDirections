package com.spacialnightmare.betterdirections.pathfinding;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class Node {
    // coordinates of the node
    private final BlockPos loc;
    // G cost = distance from starting node
    private final int GCost;
    // H cost (heuristic) = distance from end node
    private final int HCost;
    // F cost = G cost + H cost
    private int FCost;
    // parent will be the node before this one in the pathing
    private Node parent;

    public Node(BlockPos loc, int GCost, int HCost) {
        this.loc = loc;
        this.GCost = GCost;
        this.HCost = HCost;
    }

    public BlockPos getLoc() {
        return loc;
    }

    public int getGCost() {
        return GCost;
    }

    public int getHCost() {
        return HCost;
    }

    public int getFCost() {
        return getGCost() + getHCost();
    }

    public void setFCost(int FCost) {
        this.FCost = FCost;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // Override equals and hashcode so that it only checks if the coordinates are the same
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return loc.equals(node.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loc);
    }

    @Override
    public String toString() {
        return "Node{" +
                "loc=" + getLoc() +
                ", FCost=" + getFCost() +
                '}';
    }
}
