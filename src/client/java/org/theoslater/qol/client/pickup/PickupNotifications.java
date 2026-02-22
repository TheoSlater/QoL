package org.theoslater.qol.client.pickup;

import me.shedaniel.autoconfig.AutoConfig;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

import org.theoslater.qol.ModConfig;

import java.util.List;

public final class PickupNotifications {

    private static final PickupNotificationManager MANAGER = new PickupNotificationManager();
    private static final InventoryPickupDetector DETECTOR = new InventoryPickupDetector();

    private PickupNotifications() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            float textSpacing = MathHelper.clamp(config.pickupTextSpacing, 2f, 28f);
            if (!config.pickupNotificationsEnabled) {
                MANAGER.tick(textSpacing);
                return;
            }

            if (client.player != null && client.currentScreen == null && !client.player.getAbilities().creativeMode) {
                List<InventoryPickup> pickups = DETECTOR.detect(client.player.getInventory());
                if (!pickups.isEmpty()) {
                    for (InventoryPickup pickup : pickups) {
                        MANAGER.addPickup(pickup.key(), pickup.count());
                    }
                }
            }
            MANAGER.tick(textSpacing);
        });

        HudRenderCallback.EVENT.register((context, renderTickCounter) -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            if (!config.pickupNotificationsEnabled) return;

            float tickDelta = renderTickCounter.getDynamicDeltaTicks();
            float textScale = MathHelper.clamp(config.pickupTextScale, 0.5f, 1.5f);
            float textSpacing = MathHelper.clamp(config.pickupTextSpacing, 2f, 28f);
            ModConfig.PickupLocation location = config.pickupLocation;
            MANAGER.render(context, tickDelta, textScale, textSpacing, location);
        });
    }
}
