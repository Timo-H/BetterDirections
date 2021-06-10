package com.spacialnightmare.betterdirections;

import com.spacialnightmare.betterdirections.events.ModEvents;
import com.spacialnightmare.betterdirections.item.ModItems;
import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.nodes.CapabilityChunkNodes;
import com.spacialnightmare.betterdirections.nodes.NodeEventHandler;
import com.spacialnightmare.betterdirections.setup.ClientProxy;
import com.spacialnightmare.betterdirections.setup.IProxy;
import com.spacialnightmare.betterdirections.setup.ServerProxy;
import com.spacialnightmare.betterdirections.util.Config;
import com.spacialnightmare.betterdirections.util.Registration;
import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import com.spacialnightmare.betterdirections.waypoints.WaypointEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BetterDirections.MOD_ID)
public class BetterDirections
{
    public static final String MOD_ID = "betterdirections";
    // Directly reference a log4j logger.

    public static IProxy proxy;

    private static final Logger LOGGER = LogManager.getLogger();

    public BetterDirections() {
        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register Mod additions
        registerModAdditions();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        registerConfigs();

        proxy.init();

        loadConfigs();

        // Initiate the network for the mod
        ModNetwork.init();

        // Register the Capabilities
        CapabilityWaypoints.register();
        CapabilityChunkNodes.register();
         }

    private void registerConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
    }

    private void loadConfigs() {
        Config.loadConfigFile(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("betterdirections-client.toml").toString());
        Config.loadConfigFile(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve("betterdirections-server.toml").toString());
    }

    private void registerModAdditions() {
        // Inits the registration of the mod additions
        Registration.init();

        // Register the items added by the mod
        ModItems.register();

        // Register the events added by the mod
        MinecraftForge.EVENT_BUS.register(new WaypointEventHandler());
        MinecraftForge.EVENT_BUS.register(new NodeEventHandler());
        MinecraftForge.EVENT_BUS.register(new ModEvents());
    }
}
