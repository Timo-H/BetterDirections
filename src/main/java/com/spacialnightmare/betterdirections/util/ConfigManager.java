package com.spacialnightmare.betterdirections.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;

// Configuration class
public class ConfigManager {
    // Default width from left edge of window to the left edge of HudGui
    private static final int DEFAULT_HUD_X = 2;
    // Default width from top edge of window to the top edge of HudGui
    private static final int DEFAULT_HUD_Y = 2;
    // only instance of this class
    private static final ConfigManager INSTANCE;
    // the ForgeConfigsSpec instance for this mod's configuration
    private static final ForgeConfigSpec SPEC;
    // Path to the file of this mod
    private static final Path CONFIG_PATH = Paths.get("config", BetterDirections.MOD_ID + ".toml");

    static {
        Pair<ConfigManager, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(ConfigManager::new);
        INSTANCE = specPair.getLeft();
        SPEC = specPair.getRight();
        CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH)
                .sync()
                .autoreload()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        config.save();
        SPEC.setConfig(config);
    }
    // How many nodes should be generated per chunk
    private final IntValue nodesPerChunk;

    private final IntValue distanceBetweenNodes;

    private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
        nodesPerChunk = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunk.title")
                .defineInRange("nodesperchunk", 64, 16, 256);

        distanceBetweenNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.distancebetweennodes.title")
                .defineInRange("distancebetweennodes", 2, 1, 4);
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public int nodesPerChunk() {
        return nodesPerChunk.get();
    }

    public int distanceBetweenNodes() {
        return distanceBetweenNodes.get();
    }

    public void changeNodesPerChunk(int newValue) {
        nodesPerChunk.set(newValue);
        changeDistanceBetweenNodes();
    }

    public void changeDistanceBetweenNodes() {
        if (nodesPerChunk.get() == 16) {
            distanceBetweenNodes.set(4);
        } else if (nodesPerChunk.get() == 64) {
            distanceBetweenNodes.set(2);
        } else if (nodesPerChunk.get() == 256){
            distanceBetweenNodes.set(1);
        }
    }

    public void save() {
        SPEC.save();
    }

}

