package com.spacialnightmare.betterdirections.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;

public class WaypointLantern extends Block {
    // Custom Block, the waypoint lantern
    public WaypointLantern(Properties properties) {
        super(properties);
    }
    // Override the CollisionShape and return an empty Voxelshape,
    // this makes it so the block does not have any collision
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return VoxelShapes.empty();
    }
    // Override the allowsMovement and return false,
    // this makes it so the block doesnt push the player out if it is standing in the block
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
    // Block can not drop from explosions
    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }
    // Block is not harvestable
    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return false;
    }
}
