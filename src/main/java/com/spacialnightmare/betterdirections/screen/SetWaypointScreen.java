package com.spacialnightmare.betterdirections.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.SetWaypointMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public final class SetWaypointScreen extends Screen {
    // a resource location for the texture
    ResourceLocation texture = new ResourceLocation("textures/gui/book.png");

    TextFieldWidget textBox;
    // Gui width based on the texture
    private static final int GUI_WIDTH = 146;
    // Gui height based on the texture
    private static final int GUI_HEIGHT = 181;
    // Gui width offset based on the texture
    private static final int GUI_WIDTH_OFFSET = 20;
    // Gui height offset based on the texture
    private static final int GUI_HEIGHT_OFFSET = 0;
    // Button width
    private static final int BUTTON_WIDTH = 70;
    // Button height
    private static final int BUTTON_HEIGHT = 20;

    public SetWaypointScreen() {
        super(new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".set_waypoint_title"));
    }

    @Override
    protected void init() {
        // center value for the gui height
        int centerY = (this.height/2) - GUI_HEIGHT / 2;
        // add the 'Set Waypoint' button using Button values
        this.addButton(new Button((this.width/2) - BUTTON_WIDTH/2, centerY + GUI_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT,
                    new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".set_waypoint"),
                    (ButtonAction) -> {
                        // Action when button is pressed
                        if (!textBox.getText().isEmpty()) {
                            // if String taken from TextField is not empty, send it to the server with a packet
                            ModNetwork.CHANNEL.sendToServer(new SetWaypointMessage(textBox.getText()));
                            this.closeScreen();
                        }
                    }));
        // add the Exit button using Button values
        this.addButton(new Button((this.width/2) + this.width/10, centerY -20, BUTTON_WIDTH/2, BUTTON_HEIGHT,
                    new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".exit"),
                    (ButtonAction) -> {
                        this.closeScreen();
                    }));
        // add the TextField using button values
        textBox = new TextFieldWidget(minecraft.fontRenderer, (this.width/2) - BUTTON_WIDTH/2,
                centerY + GUI_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, new TranslationTextComponent(""));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // rennder the background of the Gui
        this.renderBackground(matrixStack);
        // bind the texture to the Gui
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        // gets the center values for the Gui, so it appears in the middle of the screen
        int centerX = (this.width / 2) -  GUI_WIDTH / 2;
        int centerY = (this.height/2) - GUI_HEIGHT / 2;

        // draw the title
        drawCenteredString(matrixStack, this.font, this.title.getString(),this.width / 2, centerY - 15, 0xFFFFFF);

        // bind texture again because the font changed by drawing the title
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        // Position the gui on the screen, and get the right texture
        this.blit(matrixStack, centerX, centerY, GUI_WIDTH_OFFSET, GUI_HEIGHT_OFFSET, GUI_WIDTH, GUI_HEIGHT);
        // Draw the textBox on the screen
        textBox.render(matrixStack, mouseX, mouseY, partialTicks);
        // set the textBox as focussed when the screen opens
        textBox.setFocused2(true);
        // using super to render buttons
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    // empty ButtonAction, is already defined
    private enum ButtonAction implements Button.IPressable {
        ;
        @Override
        public void onPress(Button button) {
        }
    }

    @Override
    // Action when a character is typed
    public boolean charTyped(char codePoint, int modifiers) {
        // you can now type in the textBox
        textBox.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // you can now click on the textBox
        textBox.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // adds functions for keys other than Characters
        // for example the backspace
        if (textBox.isFocused()) {
            textBox.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    // Makes it so the screen doesnt pause when the GUI is opened
    public boolean isPauseScreen() {
        return false;
    }
}
