package org.theoslater.qol.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityPose;
import org.theoslater.qol.client.QolClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class CrawlMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void enforceCrawlPose(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        if (!QolClient.forceCrawlEnabled) return;

        // Cancel crawl if jumping (Space) or sneaking (Ctrl/Shift)
        if (player.isJumping() || player.isSneaking()) {
            QolClient.forceCrawlEnabled = false;
            player.setPose(EntityPose.STANDING);
            return;
        }

        player.setPose(EntityPose.SWIMMING);
    }
}