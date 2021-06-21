package com.spacialnightmare.betterdirections.block;

import com.spacialnightmare.betterdirections.util.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<Block> WAYPOINT_LANTERN = register("waypoint_lantern",
            () -> new Block(WaypointLantern.Properties.create(Material.IRON, MaterialColor.BLUE)
                    .doesNotBlockMovement().setLightLevel((state) -> {
                        return 10;
                    }).notSolid().noDrops()));

    public static void register() {}

    private static <T extends Block>RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = Registration.BLOCKS.register(name, block);
        Registration.ITEMS.register(name, () -> new BlockItem(toReturn.get(),
                new Item.Properties().group(ItemGroup.MISC)));
        return toReturn;
    }
}
