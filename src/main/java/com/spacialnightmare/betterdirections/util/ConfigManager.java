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
    // (SliderValue) How many nodes should be generated per chunk
    private final IntValue nodesPerChunkSlider;
    // How many nodes should be generated per chunk
    private final IntValue nodesPerChunk;
    // How many blocks are between the Nodes
    private final IntValue distanceBetweenNodes;
    // (SliderValue) How large the radius for the visualization for chunkNodes is
    private final IntValue radiusChunkNodesSlider;
    // How large the radius for the visualization for chunkNodes is
    private final IntValue radiusChunkNodes;
    // HashMap that holds the values for the nodes per chunk
    private final HashMap<Integer, Integer> nodesPerChunkMap;
    // HashMap that holds the values for the distance between nodes
    private final HashMap<Integer, Integer> distanceBetweenNodesMap;
    // HashMap that holds the values for the radius for chunk nodes visualization
    private final HashMap<Integer, Integer> radiusChunkNodesMap;
    // Boolean that can be toggled, to show the calculated path with the nodes
    private final BooleanValue showCalculatedNodes;

    // initialize all the variables
    private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
        // initialize the sliders, with a defaultValue, minimum, and maximum
        nodesPerChunkSlider = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunkslider.title")
                .defineInRange("nodesperchunkslider", 2, 1, 3);

        radiusChunkNodesSlider = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.radiuschunknodesslider.title")
                .defineInRange("radiuschunknodesslider", 4, 1, 6);

        // initialize the variables
        nodesPerChunk = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunk.title")
                .defineInRange("nodesperchunk", 64, 16, 256);

        distanceBetweenNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.distancebetweennodes.title")
                .defineInRange("distancebetweennodes", 2, 1, 4);

        radiusChunkNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.radiuschunknodes.title")
                .defineInRange("radiuschunkcodes", 7, 1, 12);

        showCalculatedNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.showcalculatednodes.title")
                .define("showcalculatednodes", true);

        // initialize the HashMap's and add values to them
        radiusChunkNodesMap = new HashMap<>();
        // The first value is the slider Value, the second is the Radius Value
        radiusChunkNodesMap.put(0, 1);
        radiusChunkNodesMap.put(1, 3);
        radiusChunkNodesMap.put(2, 5);
        radiusChunkNodesMap.put(3, 7);
        radiusChunkNodesMap.put(4, 9);
        radiusChunkNodesMap.put(5, 11);

        nodesPerChunkMap = new HashMap<>();
        // The first value is the slider Value, the second is the Nodes per Chunk value
        nodesPerChunkMap.put(0, 16);
        nodesPerChunkMap.put(1, 64);
        nodesPerChunkMap.put(2, 256);

        distanceBetweenNodesMap = new HashMap<>();
        // The first value is the Nodes per Chunk, the second is the Distance between Nodes Value
        distanceBetweenNodesMap.put(16, 4);
        distanceBetweenNodesMap.put(64, 2);
        distanceBetweenNodesMap.put(256, 1);
    }
    // Getter's and Setter's for all the variables
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
        // when the slider value changes, also change the nodes per chunk
        changeNodesPerChunk(newValue);
    }
    public void changeNodesPerChunk(int slideValue) {
        // get and set the Value from the HashMap using the given value
        nodesPerChunk.set(nodesPerChunkMap.get(slideValue));
        // when the Nodes per chunk value changes, also change the distance between nodes
        changeDistanceBetweenNodes(nodesPerChunkMap.get(slideValue));
    }

    public void changeDistanceBetweenNodes(int slideValue) {
        // get and set the Value from the HashMap using the given value
        distanceBetweenNodes.set(distanceBetweenNodesMap.get(slideValue));
    }

    public void changeRadiusChunkNodesSlider(int newValue) {
        radiusChunkNodesSlider.set(newValue);
        // when the slider value changes, also change the radius for chunk nodes visualization
        changeRadiusChunkNodes(newValue);
    }

    public void changeRadiusChunkNodes(int slideValue) {
        // get and set the Value from the HashMap using the given value
        radiusChunkNodes.set(radiusChunkNodesMap.get(slideValue));
    }

    public void setShowCalculatedNodes(boolean newValue) { showCalculatedNodes.set(newValue); }

    // Save the Config values
    public void save() {
        SPEC.save();
    }
}
