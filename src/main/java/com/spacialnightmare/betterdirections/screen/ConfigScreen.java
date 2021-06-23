package com.spacialnightmare.betterdirections.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.util.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

// ConfigScreen displayed when u press on the mod Config button
// Used implementation from 'https://leo3418.github.io/2021/03/31/forge-mod-config-screen-1-16.html'
public class ConfigScreen extends Screen {
    // Distance from top of the screen to the Title
    private static final int TITLE_HEIGHT = 8;
    // Distance from top of the screen to the options row list top
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    // Distance from bottom of the screen to the options row list bottom
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    // Height of each item in the options row list
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    // Width of a button
    private static final int BUTTON_WIDTH = 200;
    // Height of a button
    private static final int BUTTON_HEIGHT = 20;
    // Distance from bottom of the screen to the "Done" button's top
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    // Get the instance of the Config Manager
    private static final ConfigManager CMI = ConfigManager.getInstance();

    // List of option rows shown on the screen
    private OptionsRowList optionsRowList;

    public ConfigScreen() {
        super(new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".configgui.title"));
    }

    @Override
    protected void init() {
        // Create the options row list
        this.optionsRowList = new OptionsRowList(this.minecraft, this.width, this.height, OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT);
        // Add a slideBar to the optionsRowList, with this slideBar you will be able to change the
        // nodePerChunk Value
        this.optionsRowList.addOption(new SliderPercentageOption("gui." + BetterDirections.MOD_ID
                + ".configgui.nodesperchunk.title",
                0.0, 2.0,
                1.0F,
                        unused -> (double) CMI.nodesPerChunkSlider(),
                (unused, newValue) -> CMI.changeNodesPerChunkSlider(newValue.intValue()),

                (gs, option) -> new StringTextComponent("Nodes generated per chunk: " + CMI.nodesPerChunk())
        ));
        // Add a BooleanOption to the optionsRowList, with this BooleanOption you will be able to toggle if the
        // algorithm takes water into account when calculating a path
        this.optionsRowList.addOption(new BooleanOption("gui." + BetterDirections.MOD_ID +
                ".configgui.ignorewater.title",
                unused -> CMI.ignoreWater(),
                (unused, newValue) -> CMI.changeIgnoreWater(newValue)
        ));

        // Add a BooleanOption to the optionsRowList, with this BooleanOption you will be able to toggle if the
        // algorithm takes the height difference going down into account when calculating a path
        this.optionsRowList.addOption(new BooleanOption("gui." + BetterDirections.MOD_ID +
                ".configgui.ignoreheightdifferencedown.title",
                unused -> CMI.IgnoreHeightDifferenceDown(),
                (unused, newValue) -> CMI.changeIgnoreHeightDifferenceDown(newValue)
        ));

        // Add a BooleanOption to the optionsRowList, with this BooleanOption you will be able to toggle if the
        // algorithm takes the height difference going up into account when calculating a path
        this.optionsRowList.addOption(new BooleanOption("gui." + BetterDirections.MOD_ID +
                ".configgui.ignoreheightdifferenceup.title",
                unused -> CMI.IgnoreHeightDifferenceUp(),
                (unused, newValue) -> CMI.changeIgnoreHeightDifferenceUp(newValue)
        ));

        // Add a BooleanOption to the optionsRowList, with this BooleanOption you will be able to toggle if the
        // algorithm takes the height difference going up into account when calculating a path
        this.optionsRowList.addOption(new BooleanOption("gui." + BetterDirections.MOD_ID +
                ".configgui.allowheightdifference.title",
                unused -> CMI.AllowHeightDifference(),
                (unused, newValue) -> CMI.changeAllowheightDifference(newValue)
        ));

        // Add the options row list as this screen's child
        this.children.add(this.optionsRowList);
        // Add the "Done" button
        this.addButton(new Button((this.width - BUTTON_WIDTH) / 2, this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT, new TranslationTextComponent("gui." + BetterDirections.MOD_ID +
                ".configgui.done"), (buttonAction) -> {
                    CMI.save();
                    this.closeScreen();
                })
        );
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Render the Background
        this.renderBackground(matrixStack);
        // Render the Options row list
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        // Render the Title
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    // empty ButtonAction, is already defined
    private enum ButtonAction implements Button.IPressable {
        ;
        @Override
        public void onPress(Button button) { }
    }
}
