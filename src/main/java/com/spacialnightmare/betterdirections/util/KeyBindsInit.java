package com.spacialnightmare.betterdirections.util;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class KeyBindsInit {
    public static KeyBinding showNodes;
    public static KeyBinding setWaypoint;
    public static KeyBinding seeWaypoints;
    public static KeyBinding togglePathing;

    public static void register(final FMLClientSetupEvent event) {
        showNodes = create("show_nodes", KeyEvent.VK_V);
        setWaypoint = create("set_waypoint", KeyEvent.VK_B);
        seeWaypoints = create("see_waypoints", KeyEvent.VK_N);
        togglePathing = create("toggle_path", KeyEvent.VK_M);

        ClientRegistry.registerKeyBinding(showNodes);
        ClientRegistry.registerKeyBinding(setWaypoint);
        ClientRegistry.registerKeyBinding(seeWaypoints);
        ClientRegistry.registerKeyBinding(togglePathing);
    }

    private static KeyBinding create(String name, int key) {
        return new KeyBinding("key." + BetterDirections.MOD_ID + "." + name, key, "key.category." +
                BetterDirections.MOD_ID);
    }

}
