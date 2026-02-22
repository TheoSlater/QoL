package org.theoslater.qol.client.pickup;

import net.minecraft.text.Text;

final class PickupNotification {

    private static final int FADE_IN_TICKS = 6;
    private static final int VISIBLE_TICKS = 20;
    private static final int FADE_OUT_TICKS = 14;
    private static final int TOTAL_TICKS = FADE_IN_TICKS + VISIBLE_TICKS + FADE_OUT_TICKS;

    private final ItemKey key;
    private final String displayName;
    private int count;
    private int age;
    private float stackOffset;

    PickupNotification(ItemKey key, int count) {
        this.key = key;
        this.displayName = key.getDisplayName().getString();
        this.count = count;
        this.stackOffset = 0f;
    }

    void tick() {
        age++;
    }

    boolean isExpired() {
        return age >= TOTAL_TICKS;
    }

    boolean canMerge(ItemKey other) {
        return key.equals(other) && age < FADE_IN_TICKS + VISIBLE_TICKS;
    }

    void merge(int extraCount) {
        count += extraCount;
        age = Math.min(age, FADE_IN_TICKS);
    }

    int getCount() {
        return count;
    }

    void approachStackOffset(float target, float smoothing) {
        float delta = target - stackOffset;
        if (Math.abs(delta) < 0.25f) {
            stackOffset = target;
            return;
        }
        stackOffset += delta * smoothing;
    }

    float getStackOffset() {
        return stackOffset;
    }

    float getAlpha(float tickDelta) {
        float currentAge = age + tickDelta;
        if (currentAge < FADE_IN_TICKS) {
            return currentAge / FADE_IN_TICKS;
        }
        if (currentAge >= FADE_IN_TICKS + VISIBLE_TICKS) {
            float fadeProgress = (currentAge - (FADE_IN_TICKS + VISIBLE_TICKS)) / FADE_OUT_TICKS;
            return Math.max(1f - fadeProgress, 0f);
        }
        return 1f;
    }

    Text createText() {
        return Text.literal("+" + count + " " + displayName);
    }
}
