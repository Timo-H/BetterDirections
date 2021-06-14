package com.spacialnightmare.betterdirections.events;

import com.spacialnightmare.betterdirections.BetterDirections;
import com.spacialnightmare.betterdirections.network.ModNetwork;
import com.spacialnightmare.betterdirections.network.message.AskWaypointMessage;
import com.spacialnightmare.betterdirections.network.message.MMessage;
import com.spacialnightmare.betterdirections.network.message.ShowNodesMessage;
import com.spacialnightmare.betterdirections.item.ModItems;
import com.spacialnightmare.betterdirections.nodes.NodeHandler;
import com.spacialnightmare.betterdirections.screen.SetWaypointScreen;
import com.spacialnightmare.betterdirections.screen.WaypointScreen;
import com.spacialnightmare.betterdirections.util.KeyBindsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDirections.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action) {
        PlayerEntity player = mc.player;
        World world = mc.world;
        if (mc.currentScreen == null && KeyBindsInit.showNodes.isPressed()) {
            // if V is pressed
            player.sendStatusMessage(new TranslationTextComponent("message.toggle_nodes"), true);
            // send a packet to the server containing a boolean to set the nodes Visible/Invisible
            ModNetwork.CHANNEL.sendToServer(new ShowNodesMessage(NodeHandler.getNodeVisibility()));
            NodeHandler.setNodeVisibility(!NodeHandler.getNodeVisibility());

        } else if (mc.currentScreen == null && KeyBindsInit.setWaypoint.isPressed()) {
            // if B is pressed
            if (world != null && player != null && world.isRemote) {
                mc.displayGuiScreen(new SetWaypointScreen());
            }
        } else if (mc.currentScreen == null && KeyBindsInit.seeWaypoints.isPressed()) {
            // if N is pressed
            if (world != null && player != null && world.isRemote) {
                mc.displayGuiScreen(new WaypointScreen());
            }

        } else if (mc.currentScreen == null && KeyBindsInit.togglePathing.isPressed()) {
            // if M is pressed
            ModNetwork.CHANNEL.sendToServer(new MMessage(key));
        }
    }

    // Activates each time the player right clicks
    @SubscribeEvent
    public void RightClick(PlayerInteractEvent.RightClickItem event) {
        if (Minecraft.getInstance().player.inventory.getCurrentItem().getItem().equals(ModItems.WAYWARD_COMPASS.get())) {
            Minecraft.getInstance().player.sendChatMessage("right clicked compass");
        }
    }
}
