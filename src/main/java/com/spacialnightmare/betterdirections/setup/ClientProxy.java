package com.spacialnightmare.betterdirections.setup;

import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDirections.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy implements IProxy{

    public static KeyBinding[] keyBindings;

    @Override
    public void init() {
        // Make keybindings for easy use of the mod
        keyBindings = new KeyBinding[4];

        keyBindings[1] = new KeyBinding("key.toggle_node_visibility.desc", 66, "key.mod_keybinds.category");
        keyBindings[0] = new KeyBinding("key.set_waypoint.desc", 86, "key.mod_keybinds.category");
        keyBindings[2] = new KeyBinding("key.remove_arrows.desc", 78, "key.mod_keybinds.category");
        keyBindings[3] = new KeyBinding("key.see_all_waypoins.desc", 77, "key.mod_keybinds.category");

        // Register the keybindings
        for (int i = 0; i < keyBindings.length; ++i) {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
