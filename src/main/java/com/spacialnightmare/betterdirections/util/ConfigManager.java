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
    // HashMap that holds the values for the nodes per chunk
    private final HashMap<Integer, Integer> nodesPerChunkMap;
    // HashMap that holds the values for the distance between nodes
    private final HashMap<Integer, Integer> distanceBetweenNodesMap;
    // Boolean that can be toggled, to ignore the water when calculating a path
    private final BooleanValue ignoreWater;
    // Boolean that can be toggled, to ignore the height difference going down when calculating a path
    private final BooleanValue ignoreHeightDifferenceDown;
    // Boolean that can be toggled, to ignore the height difference going up when calculating a path
    private final BooleanValue ignoreHeightDifferenceUp;
    // Boolean that can be toggled, to allow height differences
    private final BooleanValue mazeMode;

    // initialize all the variables
    private ConfigManager(ForgeConfigSpec.Builder configSpecBuilder) {
        // initialize the sliders, with a defaultValue, minimum, and maximum
        nodesPerChunkSlider = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunkslider.title")
                .defineInRange("nodesperchunkslider", 2, 1, 3);

        // initialize the variables
        nodesPerChunk = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.nodesperchunk.title")
                .defineInRange("nodesperchunk", 64, 16, 256);

        distanceBetweenNodes = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.distancebetweennodes.title")
                .defineInRange("distancebetweennodes", 2, 1, 4);

        ignoreWater = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.ignorewater.title")
                .define("ignorewater", false);

        ignoreHeightDifferenceDown = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.ignoreheightdifferencedown.title")
                .define("ignoreheightdifferencedown", false);

        ignoreHeightDifferenceUp = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.ignoreheightdifferenceup.title")
                .define("ignoreheightdifferenceup", false);

        mazeMode = configSpecBuilder
                .translation("gui." + BetterDirections.MOD_ID + ".configgui.mazemode.title")
                .define("mazemode", false);

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

    public boolean ignoreWater() { return ignoreWater.get(); }

    public boolean IgnoreHeightDifferenceDown() { return ignoreHeightDifferenceDown.get(); }

    public boolean IgnoreHeightDifferenceUp() { return ignoreHeightDifferenceUp.get(); }

    public boolean MazeMode() { return mazeMode.get(); }

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

    public void changeIgnoreWater(boolean newValue) { ignoreWater.set(newValue); }

    public void changeIgnoreHeightDifferenceDown(boolean newValue) { ignoreHeightDifferenceDown.set(newValue); }

    public void changeIgnoreHeightDifferenceUp(boolean newValue) { ignoreHeightDifferenceUp.set(newValue); }

    public void changeMazeMode(boolean newValue) { mazeMode.set(newValue); }

    // Save the Config values
    public void save() {
        SPEC.save();
    }
}
