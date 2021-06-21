package com.spacialnightmare.betterdirections.pathfinding;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import com.spacialnightmare.betterdirections.util.ConfigManager;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    // Boolean for visibility of the path
    public static boolean VISIBLE;
    // Get the Instance of the Config Manager
    private static final ConfigManager CMI = ConfigManager.getInstance();

    // create a path to the waypoint
    // this is an implementation of the A* pathfinding algorithm
    public static void createPath(BlockPos startPos, BlockPos endPos, World world) {
        WaypointHandler.setPath(null);
        OPEN.clear();
        CLOSED.clear();
        // create start and target node based on given coordinates
        Node startNode = new Node(startPos, 0, Heuristic(startPos, endPos));
        Node targetNode = new Node(endPos, Heuristic(startPos, endPos), 0);
        // add startNode to the OPEN arraylist to be evaluated
        OPEN.add(startNode);
        // while OPEN is not empty
        while (!OPEN.isEmpty()) {
            // set the current node to the node with the lowest FCost from the OPEN Arraylist
            Node current = OPEN.stream().min(Comparator.comparing(Node::getFCost)).get();
            // remove the current node from OPEN and add it to CLOSED
            OPEN.remove(current);
            CLOSED.add(current);
            // check the current node
            if (CheckCurrentNode(current, startNode, targetNode)) {
                break;
            }
            // for every neighbor of the current node
            ArrayList<Node> neighbours = getNeighbours(current, world, endPos);
            for (Node neighbor : neighbours) {
                CheckNeighbor(neighbor, current, world);
            }
        }
    }
    // Check current node
    public static boolean CheckCurrentNode(Node current, Node startNode, Node targetNode) {
        // if the current node == target node
        if (current.equals(targetNode)) {
            ArrayList<BlockPos> path = new ArrayList<>();
            Node node = current;
            // retrace the steps by getting the parent until the parent is the starting Node
            while (node.getParent() != null) {
                path.add(node.getParent().getLoc());
                node = node.getParent();
                if (node.equals(startNode)) {
                    break;
                }
            }
            // set the Path
            WaypointHandler.setPath(path);
            setVISIBLE(false);
            return true;
        }
        return false;
    }

    public static void CheckNeighbor(Node neighbor, Node current, World world) {
        // if neighbor is not in CLOSED
        if (!CLOSED.contains(neighbor)) {
            // if neighbor is in OPEN
            if (OPEN.contains(neighbor)) {
                // if new path to neighbor is shorter
                if (OPEN.get(OPEN.indexOf(neighbor)).getFCost() > neighbor.getFCost()) {
                    // set new FCost and set parent node to current
                    OPEN.get(OPEN.indexOf(neighbor)).setFCost(neighbor.getFCost());
                    OPEN.get(OPEN.indexOf(neighbor)).setParent(current);
                }
                // if neighbor is not in OPEN
            } else {
                // set parent node to current
                neighbor.setParent(current);
                // add neighbor to OPEN
                OPEN.add(neighbor);
            }
        }
    }

    // Calculate an estimate of how far the Node is from the Target node
    public static int Heuristic(BlockPos startPos, BlockPos endPos) {
        int distanceBetweennodes = CMI.distanceBetweenNodes();
        // Calculate the distance between coordinates
        int XDifference = (Math.abs(startPos.getX() - endPos.getX()) / distanceBetweennodes);
        int YDifference = Math.abs(startPos.getY() - endPos.getY());
        int ZDifference = (Math.abs(startPos.getZ() - endPos.getZ()) / distanceBetweennodes);
        // define the standard and diagonal step values
        int standardStep = 10;
        int diagonalStep = 14;
        // between the XDifference and ZDifference, use the smallest for the diagonal steps, and the largest - smallest
        // for the standard steps
        int heuristic = 0;
        if (XDifference < ZDifference) {
            heuristic += diagonalStep * XDifference;
            heuristic += standardStep * (ZDifference - XDifference);
        } else {
            heuristic += diagonalStep * ZDifference;
            heuristic += standardStep * (XDifference - ZDifference);
        }
        // add a value to the heuristic based on the height difference
        if (YDifference > 1) {
            heuristic += YDifference*5;
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
        int distanceBetweenNodes = CMI.distanceBetweenNodes();
        // get all the nodes from the chunk
        ArrayList<BlockPos> nodes = new ArrayList<>();
        chunk.getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY).ifPresent(capability -> {
            nodes.addAll(capability.getNodes());
        });

        // loop through coordinates in a 3 x 3 around the current node
        for (int x = current.getLoc().getX()+distanceBetweenNodes; x >= current.getLoc().getX()-distanceBetweenNodes;
             x -= distanceBetweenNodes) {
            for (int z = current.getLoc().getZ()-distanceBetweenNodes; z <= current.getLoc().getZ()+distanceBetweenNodes;
                z += distanceBetweenNodes) {

                // look if the Z and X coords match
                BlockPos matchingNode = sameChunkNeighbors(nodes, x, z);
                // if there is no match found, that means that the node we are looking for is in another chunk
                if (sameChunkNeighbors(nodes, x, z) == null) {
                    // get the node from the other chunk
                    matchingNode = differentChunkNeighbors(nodes, x, z, world);
                }

                // get the GCost for the neighbor
                int GCost = neighborGCost(current, x, z, distanceBetweenNodes, matchingNode, world);
                // add the node to the Neighbors list
                Node newNode = new Node(matchingNode, GCost, Heuristic(matchingNode, endPos));
                // add the newNode to the neighbours ArrayList
                // if it doesnt have the same coordinates as the current node
                if (!newNode.equals(current)) {
                    neighbours.add(newNode);
                }
            }
        }
        return neighbours;
    }
    // return the node with given x and z coordinates, if it doesnt exist return null
    public static BlockPos sameChunkNeighbors(ArrayList<BlockPos> nodes, int x, int z) {
        return nodes.stream().filter(nodeM -> nodeM.getX() == x && nodeM.getZ() == z)
                .findAny().orElse(null);
    }
    // load the chunk capability with the given X and Z coordinates and return the Node matching those coordinates
    public static BlockPos differentChunkNeighbors(ArrayList<BlockPos> nodes, int x, int z, World world) {
        ArrayList<BlockPos> node = new ArrayList<>();
        // we then get the chunk at those coords
        world.getChunkAt(new BlockPos(x, 0, z)).getCapability(CapabilityChunkNodes
                .CHUNK_NODES_CAPABILITY).ifPresent(capability -> {
            node.addAll(capability.getNodes());
        });
        return node.stream().filter(nodeM -> nodeM.getX() == x && nodeM.getZ() == z)
                .findAny().orElse(null);
    }
    // Calculate the GCost of the neighbor
    public static int neighborGCost(Node current, int x, int z, int distanceBetweenNodes, BlockPos matchingNode, World world) {
        int GCost = 0;
        // check if the node is adjacent in a diagonal or straight line
        // if the node has X and Z coordinates that both are 'distanceBetweenNodes' blocks away,
        // then its a diagonal neighbor, otherwise its a directly adjacent neighbor
        if (Math.abs(x - current.getLoc().getX()) == distanceBetweenNodes &&
                Math.abs(z - current.getLoc().getZ()) == distanceBetweenNodes) {
            // GCost for diagonal neighbors
            GCost += 14;
        } else {
            // GCost for directly adjacent neighbors
            GCost += 10;
        }

        // add to the GCost the ((Height Difference -1) * 40) / distance between nodes, if it is 2 or higher going uphill
        if (matchingNode.getY() - current.getLoc().getY() > 1) {
            GCost += ((Math.abs(matchingNode.getY() - current.getLoc().getY())-1) * 40) / CMI.distanceBetweenNodes();
        }
        // add to the GCost the ((Height Difference - 3) * 40) / distance between nodes, if it is 4 or higher going downhill
        if (current.getLoc().getY() - matchingNode.getY() > 3) {
            GCost += ((Math.abs(matchingNode.getY() - current.getLoc().getY())-3) * 40) / CMI.distanceBetweenNodes();
        }
        // if the top block is water, add 10 to the GCost
        if (world.getBlockState(matchingNode) == Blocks.WATER.getDefaultState()) {
            GCost += 40;
        }
        return current.getGCost()+GCost;
    }

    public static void drawNode(Node node, BlockState block, World world, Boolean draw) {
        BlockPos nodeLoc = new BlockPos(node.getLoc().getX(), 110, node.getLoc().getZ());
        NodeHandler.ShowNode(nodeLoc, world, draw, block);
    }
    // Getter and Setter for path visibility
    public static boolean isVISIBLE() {
        return !VISIBLE;
    }

    public static void setVISIBLE(boolean VISIBLE) {
        AStarPathfinding.VISIBLE = VISIBLE;
    }
}
