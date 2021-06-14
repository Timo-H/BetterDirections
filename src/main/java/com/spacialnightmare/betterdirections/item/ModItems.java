package com.spacialnightmare.betterdirections.item;

import com.spacialnightmare.betterdirections.util.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {

    // register a new item
    public static final RegistryObject<Item> WAYWARD_COMPASS
            = Registration.ITEMS.register("wayward_compass",
            () -> new Item(new Item.Properties().group(ItemGroup.TOOLS)));

    public static void register() { }
}
