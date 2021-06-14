package com.spacialnightmare.betterdirections.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.RemoveWaypointMesage;
import com.spacialnightmare.betterdirections.waypoints.CapabilityWaypoints;
import com.spacialnightmare.betterdirections.waypoints.WaypointHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;


public class WaypointScreen extends Screen {

    // A resource Location for the texture
    ResourceLocation texture = new ResourceLocation("textures/gui/book.png");

    ArrayList<TextFieldWidget> waypointsTextbox = new ArrayList<>();

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
    // Button offset
    private static final int BUTTON_OFFSET = 15;

    public WaypointScreen() {
        super(new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".waypoint_title"));
    }

    @Override
    protected void init() {
        // center value for the gui height
        int centerY = (this.height/2) - GUI_HEIGHT / 2;

        // add the Exit button using Button values
        this.addButton(new Button((this.width/2) + 30, centerY -20, BUTTON_WIDTH/2, BUTTON_HEIGHT,
                new TranslationTextComponent("gui." + BetterDirections.MOD_ID + ".exit"),
                (ButtonAction) -> {
                    this.closeScreen();
                }));

        // get the player Waypoints Capability
        this.minecraft.player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
            // check if the player has any waypoints
            if (h.getWaypoints() != null && h.getWaypoints().size() > 0) {
                // for every waypoint
                for (int i = 0; i < h.getWaypoints().size(); i++) {
                    // create a textBox for the waypoint
                    this.waypointsTextbox.add(new TextFieldWidget(minecraft.fontRenderer, (this.width/2) -
                            BUTTON_HEIGHT*3 + BUTTON_HEIGHT/4, centerY + BUTTON_OFFSET +
                            (i*(BUTTON_HEIGHT + BUTTON_HEIGHT/4)), BUTTON_WIDTH-BUTTON_HEIGHT/2, BUTTON_HEIGHT,
                            new TranslationTextComponent("text." + BetterDirections.MOD_ID + "." +
                                    h.getWaypointsNames().get(i))));
                    // set the textBox name and color
                    this.waypointsTextbox.get(i).setText(h.getWaypointsNames().get(i));
                    this.waypointsTextbox.get(i).setDisabledTextColour(0xFFFFFF);

                    // create a button to start/stop the pathing to the waypoint
                    PathingButton pathing = new PathingButton((this.width/2) + BUTTON_HEIGHT*4, centerY + BUTTON_OFFSET +
                            (i*(BUTTON_HEIGHT + BUTTON_HEIGHT/4)), (ButtonAction) -> {
                    }, i);
                    // add the button to the button List
                    addButton(pathing);
                    // create a button to delete a waypoint
                    DeleteButton delete = new DeleteButton((this.width/2) + BUTTON_HEIGHT*2, centerY + BUTTON_OFFSET +
                            (i*(BUTTON_HEIGHT + BUTTON_HEIGHT/4)), (ButtonAction) -> {
                    }, i);
                    // add the button to the button List
                    addButton(delete);
                }
            }
        });

    }

    // renders all the objects of the gui
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

        // draw all the TextBoxes in the ArrayList on the screen
        if (!this.waypointsTextbox.isEmpty()) {
            for (TextFieldWidget waypoint : this.waypointsTextbox) {
                waypoint.setEnabled(false);
                waypoint.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }

        // draw all the Buttons in the list on the screen
        if (!this.buttons.isEmpty()) {
            for (Widget button : this.buttons) {
                button.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }
    }

    // empty ButtonAction, is already defined in the onPress method
    private enum ButtonAction implements Button.IPressable {
        ;
        @Override
        public void onPress(Button button) { }
    }

    // Makes it so the screen doesnt pause when the GUI is opened
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        Minecraft.getInstance().player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(h -> {
            if (h.getWaypoints().size() != this.waypointsTextbox.size()) {
                this.init();
            }
        });
    }

    static class PathingButton extends Button {
        // textureY is will need to be changed depending on what texture is used
        public int textureY;
        // waypoint index value
        public int waypoint;
        // ResourceLocation for the texture
        ResourceLocation texture = new ResourceLocation("textures/gui/book.png");

        public PathingButton(int x, int y, IPressable pressedAction, int waypoint) {
            super(x, y, 18, 10, new TranslationTextComponent(""), pressedAction);
            this.waypoint = waypoint;
        }

        // render all the parts of the button
        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

            // texture values for the button
            int textureX = 3;
            int textureWidth = 18;
            int textureHeight = 10;

            if (visible) {
                // bind the texture to the button
                Minecraft.getInstance().getTextureManager().bindTexture(texture);

                // check if the mouse is hovering over the button
                boolean isHovered = (mouseX >= x
                        && mouseY >= y
                        && mouseX < x+width
                        && mouseY < y+height);

                // if the mouse is hovering over the button, change the X texture value, to get a different texture
                if (isHovered) {
                    textureX = 26;
                }

                // position the button with values
                this.blit(matrixStack, x, y, textureX, getTextureY(),
                        textureWidth, textureHeight);
            }
        }

        // get TextureY based on other values
        public int getTextureY() {
            Minecraft.getInstance().player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY)
                    .ifPresent(capability -> {
                        // checks if the path button pressed, is the same one that is currently active
                        // then changes the Y texture value to get a different texture
                        if (capability.getWaypointsNames().indexOf(WaypointHandler.getIsPathingTo()) == waypoint) {
                            this.textureY = 194;
                        } else {
                            this.textureY = 207;
                        }
                    });
            return this.textureY;
        }

        // action taken when the button is pressed
        @Override
        public void onPress() {
            Minecraft.getInstance().player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY)
                    .ifPresent(capability -> {
                        // checks if the path button pressed, is the same one that is currently active, and if so,
                        // switch the boolean value. Also switches the boolean if there was no previous path active
                        if (capability.getWaypointsNames().indexOf(WaypointHandler.getIsPathingTo()) == waypoint ||
                        WaypointHandler.getIsPathingTo() == null || WaypointHandler.getIsPathingTo().equals("")) {
                            WaypointHandler.setPathing(!WaypointHandler.isPathing());
                        }
                        // check if there is a path active
                        if (WaypointHandler.isPathing()) {
                            // set the PathingTo String to the name of the destination waypoint
                            WaypointHandler.setIsPathingTo(capability.getWaypointsNames().get(waypoint));
                        } else {
                            // set the PathingTo String to an empty string
                            WaypointHandler.setIsPathingTo("");
                        }
                });
        }
    }
    // Create a delete button for the waypoints
    static class DeleteButton extends Button {
        // index value for waypoint
        public int waypoint;
        // resourceLocation for the texture
        ResourceLocation texture = new ResourceLocation("textures/gui/book.png");

        public DeleteButton(int x, int y, IPressable pressedAction, int waypoint) {
            super(x, y, 8, 9, new TranslationTextComponent(""), pressedAction);
            this.waypoint = waypoint;
        }
        // render all the parts of the button
        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

            // texture values for the button
            int textureX = 36;
            int textureY = 194;
            int textureWidth = 8;
            int textureHeight = 9;

            if (visible) {
                // bind the texture to the button
                Minecraft.getInstance().getTextureManager().bindTexture(texture);
            }
            // position the button with values
            this.blit(matrixStack, x, y, textureX, textureY, textureWidth, textureHeight);
        }
        // action taken when pressed
        @Override
        public void onPress() {
            // send a packet to the server telling it to remove the waypoint, using its index value
            Minecraft.getInstance().player.getCapability(CapabilityWaypoints.WAYPOINTS_CAPABILITY).ifPresent(capability -> {
                ModNetwork.CHANNEL.sendToServer(new RemoveWaypointMesage(capability.getWaypointsNames().get(waypoint)));

            });
        }
    }
}
