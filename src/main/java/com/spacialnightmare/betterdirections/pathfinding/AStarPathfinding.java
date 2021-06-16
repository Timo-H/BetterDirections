package com.spacialnightmare.betterdirections.pathfinding;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.util.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Comparator;

public class AStarPathfinding {
    // set of nodes to be evaluated
    public static ArrayList<Node> OPEN = new ArrayList<>();
    // set of nodes already evaluated
    public static ArrayList<Node> CLOSED = new ArrayList<>();

    // create a path to the waypoint
    public static void createPath(BlockPos startPos, BlockPos endPos, World world) {
        // create start and target node based on given coordinates
        Node startNode = new Node(startPos, 0, Heuristic(startPos, endPos));
        Node targetNode = new Node(endPos, Heuristic(startPos, endPos), 0);
        // add startNode to the OPEN arraylist to be evaluated
        OPEN.add(startNode);
        // if the target node is not reached
        while (!OPEN.isEmpty()) {
            // set the current node to the node with the lowest FCost from the OPEN Arraylist
            Node current = OPEN.stream().min(Comparator.comparing(Node::getFCost)).get();
            // remove the current node from OPEN and add it to CLOSED
            OPEN.remove(current);
            CLOSED.add(current);
            // if the current node == target node then set endReached as true
            if (current == targetNode) {
                break;
            }
            // for every neighbor of the current node
            for (Node neighbour : getNeighbours(current, world, endPos)) {
                // if neighbor is not in CLOSED
                if (!CLOSED.contains(neighbour)) {
                    // if neighbor is in OPEN
                    if (OPEN.contains(neighbour)) {
                        // if new path to neighbor is shorter
                        if (OPEN.get(OPEN.indexOf(neighbour)).getFCost() > neighbour.getFCost()) {
                            // set new FCost and set parent node to current
                            OPEN.get(OPEN.indexOf(neighbour)).setFCost(neighbour.getFCost());
                            OPEN.get(OPEN.indexOf(neighbour)).setParent(current);
                        }
                    // if neighbor is not in OPEN
                    } else {
                        // set new FCost and set parent node to current
                        OPEN.get(OPEN.indexOf(neighbour)).setFCost(neighbour.getFCost());
                        OPEN.get(OPEN.indexOf(neighbour)).setParent(current);
                        // add neighbor to OPEN
                        OPEN.add(neighbour);
                    }
                }
            }
        }
    }
    // Calculate an estimate of how far the Node is from the Target node
    public static int Heuristic(BlockPos startPos, BlockPos endPos) {
        // Calculate the distance between coordinates
        int XDifference = Math.abs(startPos.getX() - endPos.getX()) / 3;
        int YDifference = Math.abs(startPos.getY() - endPos.getY());
        int ZDifference = Math.abs(startPos.getZ() - endPos.getZ()) / 3;
        // define the standard and diagonal step values
        int standardStep = 10 + (YDifference * 10);
        int diagonalStep = 14 + (YDifference * 10);
        // between the XDifference and ZDifference, use the smallest for the diagonal steps, and the largest - smallest
        // for the standard steps
        int heuristic = 0;
        if (XDifference < ZDifference) {
            for (int i = 0; i < ZDifference; i++) {
                heuristic += diagonalStep * XDifference;
                heuristic += standardStep * (ZDifference - XDifference);
            }
        } else {
            for (int i = 0; i < XDifference; i++) {
                heuristic += diagonalStep * ZDifference;
                heuristic += standardStep * (XDifference - ZDifference);
            }
        }
        return heuristic;
    }
    // return all the neighbours of the given node
    public static ArrayList<Node> getNeighbours(Node current, World world, BlockPos endPos) {
        // get the chunk from the current Node
        Chunk chunk = world.getChunkAt(current.getLoc());
        // make an Arraylist for the neighbour nodes
        ArrayList<Node> neighbours = new ArrayList<>();
        // get the Distance between nodes from the Config
        int distanceBetweenNodes = Config.DistanceBetweenNodes();
        // get all the nodes from the chunk
        ArrayList<BlockPos> nodes = new ArrayList<>();
        chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(capability -> {
            nodes.addAll(capability.getNodes());
        });
        // loop through coordinates in a 3 x 3 around the current node
        for (int x = current.getLoc().getX()+distanceBetweenNodes; x > current.getLoc().getX()-distanceBetweenNodes;
             x -= distanceBetweenNodes) {
            for (int z = current.getLoc().getZ()-distanceBetweenNodes; z < current.getLoc().getZ()+distanceBetweenNodes;
                z += distanceBetweenNodes) {

                // check if the node is adjacent in a diagonal or straight line
                int GCost;
                if (Math.abs(x - current.getLoc().getX()) == distanceBetweenNodes &&
                        Math.abs(z - current.getLoc().getZ()) == distanceBetweenNodes) {
                    // GCost for diagonal neighbors
                    GCost = 14;
                } else {
                    // GCost for directly adjacent neighbors
                    GCost = 10;
                }

                int finalX = x;
                int finalZ = z;
                // look if the Z and X coords match
                BlockPos matchingNode = nodes.stream().filter(nodeM -> nodeM.getX() == finalX && nodeM.getZ() == finalZ)
                        .findAny().orElse(null);
                // if there is no match found, that means that the node we are looking for is in another chunk
                if (matchingNode == null) {
                    ArrayList<BlockPos> node = new ArrayList<>();
                    // we then get the chunk at those coords
                    world.getChunkAt(new BlockPos(finalX, 0, finalZ)).getCapability(CapabilityChunkNodes
                            .CHUNK_NODES_CAPABILITY).ifPresent(capability -> {
                                node.addAll(capability.getNodes());
                    });
                    // get the node from this chunk
                    matchingNode = node.stream().filter(nodeM -> nodeM.getX() == finalX && nodeM.getZ() == finalZ)
                            .findAny().orElse(null);
                }
                // add the node to the Neighbors list
                neighbours.add(new Node(matchingNode, current.getGCost()+GCost, Heuristic(current.getLoc(), endPos)));
            }
        }
        return neighbours;
    }
}
