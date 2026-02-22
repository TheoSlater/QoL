package org.theoslater.qol.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityPose;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;

import org.theoslater.qol.ModConfig;
import org.theoslater.qol.client.pickup.PickupNotifications;

public class QolClient implements ClientModInitializer {

    private static KeyBinding crawlToggleKey;
    public static boolean forceCrawlEnabled = false;

    @Override
    public void onInitializeClient() {


        try {
            AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        } catch (RuntimeException ignored) {
        }

        crawlToggleKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.qol.toggle_crawl",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_C, KeyBinding.Category.MOVEMENT

                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (crawlToggleKey.wasPressed()) {
                toggleCrawl(client);
            }
        });
        PickupNotifications.init();
    }

    private void toggleCrawl(MinecraftClient client) {
        if (client == null || client.player == null) return;

        forceCrawlEnabled = !forceCrawlEnabled;

        if (forceCrawlEnabled) {
            client.player.setPose(EntityPose.SWIMMING);
//            client.player.sendMessage(Text.literal("§aForced Crawl: ON"), true);
        } else {
            client.player.setPose(EntityPose.STANDING);
//            client.player.sendMessage(Text.literal("§cForced Crawl: OFF"), true);
        }
    }
}
