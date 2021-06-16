package com.spacialnightmare.betterdirections.network.message;

import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.pathfinding.AStarPathfinding;
import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CreatePathMessage {
    public int waypointIndex;

    public CreatePathMessage() { }

    public CreatePathMessage(int waypointIndex) { this.waypointIndex = waypointIndex; }

    // Encoder for this packet
    public static void encode(CreatePathMessage message, PacketBuffer buffer) { buffer.writeInt(message.waypointIndex); }
    // Decoder for this packet
    public static CreatePathMessage decode(PacketBuffer buffer) {
        return new CreatePathMessage(buffer.readInt());
    }
    // Action performed by this packet
    public static void handle(CreatePathMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // get player and world
            ServerPlayerEntity player = context.getSender();
            World world = player.getEntityWorld();
            // get all the nodes from the chunk the player is currently standing in
            ArrayList<BlockPos> playerChunkNodes = new ArrayList<>();
            world.getChunkAt(player.getPosition()).getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY)
                    .ifPresent(capability -> {
                        playerChunkNodes.addAll(capability.getNodes());
                    });
            // variable for the closest node to the player position
            BlockPos closestNodeToPlayer = null;
            // for every node in the chunk
            for (BlockPos Startnode : playerChunkNodes) {
                // if closestNodeToPlayer is null, or if the distance to that node (difference in X coord + difference
                // in Y coord) is smaller
                if (closestNodeToPlayer == null || Math.abs(Startnode.getX() - player.getPosition().getX()) +
                        Math.abs(Startnode.getZ() - player.getPosition().getZ()) < Math.abs(closestNodeToPlayer.getX()
                        - player.getPosition().getX()) + Math.abs(closestNodeToPlayer.getZ() - player.getPosition().getZ())) {
                    // replace the closestNodeToPlayer with the current node
                    closestNodeToPlayer = Startnode;
                }
            }

            // get the waypoint coordinates using the waypointIndex
            ArrayList<BlockPos> waypointArray = new ArrayList<>();
            player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {
                waypointArray.add(capability.getWaypoints().get(message.waypointIndex));
            });
            BlockPos waypoint = waypointArray.get(0);
            // get all the nodes from the chunk the waypoint is in
            ArrayList<BlockPos> waypointChunkNodes = new ArrayList<>();
            world.getChunkAt(waypoint).getCapability(CapabilityChunkNodes.CHUNK_NODES_CAPABILITY)
                    .ifPresent(capability -> {
                        waypointChunkNodes.addAll(capability.getNodes());
                    });
            // variable for closest node to the waypoint
            BlockPos closestNodeToWaypoint = null;
            // for every node in the chunk
            for (BlockPos Targetnode : waypointChunkNodes) {
                // if closestNodeToWaypoint is null, or if the distance to that node (difference in X coord + difference
                // in Y coord) is smaller
                if (closestNodeToWaypoint == null || Math.abs(Targetnode.getX() - waypoint.getX()) + Math.abs(Targetnode.getZ() -
                        waypoint.getZ()) < Math.abs(closestNodeToWaypoint.getX() - waypoint.getX()) +
                        Math.abs(closestNodeToWaypoint.getZ() - waypoint.getZ())) {
                    // replace the closestNodeToWaypoint with the current node
                    closestNodeToWaypoint = Targetnode;
                }
            }
            // Create a path using the closest node to the player as the starting node, and the closest node to
            // the waypoint as the target node
            AStarPathfinding.createPath(closestNodeToPlayer, closestNodeToWaypoint, world);

        });
        context.setPacketHandled(true);
    }
}
