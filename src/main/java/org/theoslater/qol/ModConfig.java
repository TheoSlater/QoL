package org.theoslater.qol;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.AutoConfig;

@Config(name = "qol")
public class ModConfig implements ConfigData {

    public enum PickupLocation {
        BOTTOM_CENTER,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    static {
        try {
            AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        } catch (Exception e) {
            // already registered1
        }
    }

    public boolean autoSortEnabled = true;
    public boolean pickupNotificationsEnabled = true;
    public float pickupTextScale = 1f;
    public float pickupTextSpacing = 10f;
    public PickupLocation pickupLocation = PickupLocation.BOTTOM_RIGHT;
}
