package com.spacialnightmare.betterdirections.gui.button;

import net.minecraft.client.gui.widget.button.Button;

public class SetWaypoint implements Button.IPressable {
    @Override
    public void onPress(Button button) {
        System.out.println("Waypoint set");
    }
}
