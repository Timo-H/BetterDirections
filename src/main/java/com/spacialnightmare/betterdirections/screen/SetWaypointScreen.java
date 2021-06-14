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

    ResourceLocation texture = new ResourceLocation("textures/gui/book.png");

    TextFieldWidget textBox;

    private static final int GUI_WIDTH = 146;

    private static final int GUI_HEIGHT = 181;

    private static final int GUI_WIDTH_OFFSET = 20;

    private static final int GUI_HEIGHT_OFFSET = 0;

    private static final int BUTTON_WIDTH = 70;

    private static final int BUTTON_HEIGHT = 20;

    public SetWaypointScreen() {
        super(new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".set_waypoint_title"));
    }

    @Override
    protected void init() {
        int centerY = (this.height/2) - GUI_HEIGHT / 2;

        this.addButton(new Button((this.width/2) - BUTTON_WIDTH/2, centerY + GUI_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT,
                    new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".set_waypoint"),
                    (ButtonAction) -> {
                        if (!textBox.getText().isEmpty()) {
                            ModNetwork.CHANNEL.sendToServer(new SetWaypointMessage(textBox.getText()));
                            this.closeScreen();
                        }
                    }));

        this.addButton(new Button((this.width/2) + this.width/10, centerY -20, BUTTON_WIDTH/2, BUTTON_HEIGHT,
                    new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".exit"),
                    (ButtonAction) -> {
                        this.closeScreen();
                    }));

        textBox = new TextFieldWidget(minecraft.fontRenderer, (this.width/2) - BUTTON_WIDTH/2,
                centerY + GUI_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, new TranslationTextComponent(""));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);

        int centerX = (this.width / 2) -  GUI_WIDTH / 2;
        int centerY = (this.height/2) - GUI_HEIGHT / 2;

        drawCenteredString(matrixStack, this.font, this.title.getString(),this.width / 2, centerY - 15, 0xFFFFFF);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        this.blit(matrixStack, centerX, centerY, GUI_WIDTH_OFFSET, GUI_HEIGHT_OFFSET, GUI_WIDTH, GUI_HEIGHT);
        textBox.render(matrixStack, mouseX, mouseY, partialTicks);
        textBox.setFocused2(true);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private enum ButtonAction implements Button.IPressable {

        ButtonAction() {};

        @Override
        public void onPress(Button button) {
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        textBox.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        textBox.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textBox.isFocused()) {
            textBox.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
