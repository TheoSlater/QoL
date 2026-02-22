package org.theoslater.qol.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SplashPotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({PotionItem.class, SplashPotionItem.class, LingeringPotionItem.class})
public class PotionItemMixin {

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static Item.Settings iHateMojang(Item.Settings settings) {
        return settings.maxCount(16);
    }
}