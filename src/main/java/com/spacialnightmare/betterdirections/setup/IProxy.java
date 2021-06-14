package com.spacialnightmare.betterdirections.setup;

import net.minecraft.world.World;

// Interface for Proxy's
public interface IProxy {

    void init();

    World getClientWorld();
}
