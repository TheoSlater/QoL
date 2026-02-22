package org.theoslater.qol.client.pickup;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

final class ItemKey {

    private final ItemStack snapshot;

    ItemKey(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        this.snapshot = copy;
    }

    ItemStack toDisplayStack() {
        return snapshot.copy();
    }

    Text getDisplayName() {
        return snapshot.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemKey itemKey)) return false;
        return ItemStack.areItemsAndComponentsEqual(snapshot, itemKey.snapshot);
    }

    @Override
    public int hashCode() {
        return ItemStack.hashCode(snapshot);
    }
}
