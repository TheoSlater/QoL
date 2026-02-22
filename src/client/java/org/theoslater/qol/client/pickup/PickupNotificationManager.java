package org.theoslater.qol.client.pickup;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import org.joml.Matrix3x2fStack;

import org.theoslater.qol.ModConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class PickupNotificationManager {

    private static final float BASE_Y_OFFSET = 60f;
    private static final float SIDE_MARGIN = 10f;

    private final List<PickupNotification> notifications = new ArrayList<>();

    void addPickup(ItemKey key, int amount) {
        for (PickupNotification notification : notifications) {
            if (notification.canMerge(key)) {
                notification.merge(amount);
                return;
            }
        }
        notifications.add(new PickupNotification(key, amount));
    }

    void tick(float textSpacing) {
        Iterator<PickupNotification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            PickupNotification notification = iterator.next();
            notification.tick();
            if (notification.isExpired()) {
                iterator.remove();
            }
        }
        updateOffsets(textSpacing);
    }

    private void updateOffsets(float textSpacing) {
        if (notifications.isEmpty()) return;
        float spacing = Math.max(textSpacing, 2f);
        for (int idx = notifications.size() - 1; idx >= 0; idx--) {
            int stackIndex = notifications.size() - 1 - idx;
            float target = stackIndex * spacing;
            notifications.get(idx).approachStackOffset(target, 0.35f);
        }
    }

    void render(DrawContext context, float tickDelta, float textScale, float textSpacing, ModConfig.PickupLocation location) {
        if (notifications.isEmpty()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        float baseY = height - BASE_Y_OFFSET;

        Matrix3x2fStack matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.scale(textScale, textScale);

        float scaledWidth = width / textScale;

        for (int idx = notifications.size() - 1; idx >= 0; idx--) {
            PickupNotification notification = notifications.get(idx);
            float alpha = MathHelper.clamp(notification.getAlpha(tickDelta), 0f, 1f);
            int alphaInt = MathHelper.ceil(alpha * 255f);
            int color = (alphaInt << 24) | 0xFFFFFF;

            Text text = notification.createText();
            int textWidth = textRenderer.getWidth(text);
            float x;
            float margin = SIDE_MARGIN / textScale;

            switch (location) {
                case BOTTOM_LEFT -> x = margin;
                case BOTTOM_RIGHT -> x = scaledWidth - textWidth - margin;
                default -> x = scaledWidth / 2f - textWidth / 2f;
            }

            int stackIndex = notifications.size() - 1 - idx;
            float yActual = baseY - notification.getStackOffset();
            float y = yActual / textScale;

            context.drawText(textRenderer, text, MathHelper.floor(x), MathHelper.floor(y), color, true);
        }

        matrices.popMatrix();
    }
}
