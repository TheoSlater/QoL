package org.theoslater.qol.client.pickup;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class InventoryPickupDetector {

    private final Map<ItemKey, Integer> previousTotals = new HashMap<>();
    private boolean initialized;

    List<InventoryPickup> detect(PlayerInventory inventory) {
        if (!initialized) {
            previousTotals.clear();
            previousTotals.putAll(accumulate(inventory));
            initialized = true;
            return List.of();
        }

        Map<ItemKey, Integer> currentTotals = accumulate(inventory);
        List<InventoryPickup> pickups = new ArrayList<>();

        for (Map.Entry<ItemKey, Integer> entry : currentTotals.entrySet()) {
            int previous = previousTotals.getOrDefault(entry.getKey(), 0);
            int current = entry.getValue();
            if (current > previous) {
                pickups.add(new InventoryPickup(entry.getKey(), current - previous));
            }
        }

        previousTotals.clear();
        previousTotals.putAll(currentTotals);
        return pickups;
    }

    private Map<ItemKey, Integer> accumulate(PlayerInventory inventory) {
        Map<ItemKey, Integer> counts = new HashMap<>();
        for (int i = 0; i < inventory.size(); i++) {
            accumulate(counts, inventory.getStack(i));
        }
        return counts;
    }

    private void accumulate(Map<ItemKey, Integer> counts, ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemKey key = new ItemKey(stack);
        counts.merge(key, stack.getCount(), Integer::sum);
    }
}
