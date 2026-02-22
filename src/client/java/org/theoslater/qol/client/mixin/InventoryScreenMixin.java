package org.theoslater.qol.client.mixin;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import me.shedaniel.autoconfig.AutoConfig;
import org.theoslater.qol.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void autoSortInventory(CallbackInfo ci) {
        if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().autoSortEnabled) return;
        if (this.client == null || this.client.player == null) return;

        PlayerInventory inv = this.client.player.getInventory();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 9; i < 36; i++) {
            items.add(inv.getStack(i).copy());
        }

        items.sort(Comparator.comparing(stack -> {
            if (stack.isEmpty()) return "zzz_empty";
            return Registries.ITEM.getId(stack.getItem()).toString();
        }));

        int slot = 9;
        for (ItemStack stack : items) {
            inv.setStack(slot++, stack);
        }
    }
}