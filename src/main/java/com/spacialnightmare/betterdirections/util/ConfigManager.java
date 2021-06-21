package com.spacialnightmare.betterdirections.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.spacialnightmare.betterdirections.BetterDirections;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

// Configuration class
// Used implementation from 'https://leo3418.github.io/2021/03/31/forge-mod-config-screen-1-16.html'
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
    private final IntValue nodesPerChunkSlider;

    private final IntValue nodesPerChunk;

    private final IntValue distanceBetweenNodes;

    private final IntValue radiusChunkNodesSlider;

    private final IntValue radiusChunkNodes;

    private final HashMap<Integer, Integer> nodesPerChunkMapF;

    private final HashMap<Integer, Integer> nodesPerChunkMapI;

    private final HashMap<Integer, Integer> radiusChunkNodesMap;

    private final BooleanValue showCalculatedNodes;

    private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
        nodesPerChunkSlider = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunkslider.title")
                .defineInRange("nodesperchunkslider", 2, 1, 3);

        nodesPerChunk = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunk.title")
                .defineInRange("nodesperchunk", 64, 16, 256);

        distanceBetweenNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.distancebetweennodes.title")
                .defineInRange("distancebetweennodes", 2, 1, 4);

        radiusChunkNodesSlider = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.radiuschunknodesslider.title")
                .defineInRange("radiuschunknodesslider", 4, 1, 6);

        radiusChunkNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.radiuschunknodes.title")
                .defineInRange("radiuschunkcodes", 7, 1, 12);

        showCalculatedNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.showcalculatednodes.title")
                .define("showcalculatednodes", true);

        radiusChunkNodesMap = new HashMap<>();
        radiusChunkNodesMap.put(0, 1);
        radiusChunkNodesMap.put(1, 3);
        radiusChunkNodesMap.put(2, 5);
        radiusChunkNodesMap.put(3, 7);
        radiusChunkNodesMap.put(4, 9);
        radiusChunkNodesMap.put(5, 11);

        nodesPerChunkMapF = new HashMap<>();
        nodesPerChunkMapF.put(0, 16);
        nodesPerChunkMapF.put(1, 64);
        nodesPerChunkMapF.put(2, 256);

        nodesPerChunkMapI = new HashMap<>();
        nodesPerChunkMapI.put(16, 4);
        nodesPerChunkMapI.put(64, 2);
        nodesPerChunkMapI.put(256, 1);
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public int nodesPerChunkSlider() {
        return nodesPerChunkSlider.get();
    }

    public int nodesPerChunk() {
        return nodesPerChunk.get();
    }

    public int distanceBetweenNodes() {
        return distanceBetweenNodes.get();
    }

    public int radiusChunkNodesSlider() { return radiusChunkNodesSlider.get(); }

    public int radiusChunkNodes() { return radiusChunkNodes.get(); }

    public boolean showCalculatedNodes() { return showCalculatedNodes.get(); }

    public void changeNodesPerChunkSlider(int newValue) {
        nodesPerChunkSlider.set(newValue);
        changeNodesPerChunk(newValue);
    }
    public void changeNodesPerChunk(int slideValue) {
        nodesPerChunk.set(nodesPerChunkMapF.get(slideValue));
        changeDistanceBetweenNodes(nodesPerChunkMapF.get(slideValue));
    }

    public void changeDistanceBetweenNodes(int slideValue) {
        distanceBetweenNodes.set(nodesPerChunkMapI.get(slideValue));
    }

    public void changeRadiusChunkNodesSlider(int newValue) {
        radiusChunkNodesSlider.set(newValue);
        changeRadiusChunkNodes(newValue);
    }

    public void changeRadiusChunkNodes(int slideValue) {
        radiusChunkNodes.set(radiusChunkNodesMap.get(slideValue));
    }

    public void setShowCalculatedNodes(boolean newValue) { showCalculatedNodes.set(newValue); }

    public void save() {
        SPEC.save();
    }

}

