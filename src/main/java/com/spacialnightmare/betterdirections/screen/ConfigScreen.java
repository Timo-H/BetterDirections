package com.spacialnightmare.betterdirections.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.util.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.SliderMultiplierOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    // The parent screen of this screen
    private final Screen parentScreen;

    // List of option rows shown on the screen
    private OptionsRowList optionsRowList;

    public ConfigScreen(Screen parentScreen) {
        super(new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".configgui.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        // Create the options row list
        this.optionsRowList = new OptionsRowList(this.minecraft, this.width, this.height, OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT);

        this.optionsRowList.addOption(new SliderMultiplierOption("gui." + BetterDirections.MOD_ID
                + ".configgui.nodesperchunk.title",
                16, 256,
                4,
                        unused -> (double) CMI.nodesPerChunk(),
                (unused, newValue) -> CMI.changeNodesPerChunk(newValue.intValue()),

                (gs, option) -> new StringTextComponent("Nodes generated per chunk: " + (int) option.get(gs)
                )
        ));

        // Add the options row list as this screen's child
        this.children.add(this.optionsRowList);
        // Add the "Done" button
        this.addButton(new Button((this.width - BUTTON_WIDTH) / 2, this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT, new TranslationTextComponent("gui." + BetterDirections.MOD_ID +
                ".configgui.done"), button -> this.onClose())
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

    @Override
    public void onClose() {
        CMI.save();
    }
}
