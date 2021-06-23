package com.spacialnightmare.tests;

import com.spacialnightmare.betterdirections.pathfinding.AStarPathfinding;
import com.spacialnightmare.betterdirections.pathfinding.Node;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Test functies voor het algoritme, een aantal functies kon ik geen test geven omdat die variabelen nodig hebben die
// ik niet kan namaken/simuleren, zoals de minecraft 'World'.
class AStarPathfindingTest {

    Node startNode;
    Node targetNode;
    Node currentNode;
    Node pathNode1;
    Node pathNode2;
    Node pathNode3;

    @BeforeEach
    public void setup() {
        startNode = new Node(new BlockPos(1, 1, 1), 0, 40);
        targetNode = new Node(new BlockPos(5, 5, 5), 40, 0);
        currentNode = new Node(new BlockPos(2, 2, 2), 10, 30);
        pathNode1 = new Node(new BlockPos(3, 3, 3), 20, 20);
        pathNode2 = new Node(new BlockPos(4, 4, 4), 30, 10);
        pathNode3 = new Node(new BlockPos(5, 5, 5), 40, 0);
    }

    @Test
    public void checkCurrentNode() {
        boolean actual_value = AStarPathfinding.CheckCurrentNode(currentNode, startNode, targetNode);
        Assertions.assertFalse(actual_value);

        actual_value = AStarPathfinding.CheckCurrentNode(pathNode3, startNode, targetNode);
        Assertions.assertTrue(actual_value);
    }

    @Test
    public void checkNeighbor() {
        AStarPathfinding.CheckNeighbor(pathNode1, currentNode);
        Assertions.assertEquals(pathNode1.getParent(), currentNode);
    }

    @Test
    public void heuristic() {
        int expected_value = 48;
        int actual_value = AStarPathfinding.Heuristic(startNode.getLoc(), targetNode.getLoc());
        System.out.println(actual_value);
        Assertions.assertEquals(expected_value, actual_value);
    }
}