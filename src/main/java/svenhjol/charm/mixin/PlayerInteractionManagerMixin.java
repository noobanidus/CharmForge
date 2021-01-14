package svenhjol.charm.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.Acquisition;

@Mixin(PlayerInteractionManager.class)
public class PlayerInteractionManagerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "tryHarvestBlock", at = @At("HEAD"))
    private void hookTryBreakBlockBegin(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Acquisition.startBreaking(player);
    }

    @Inject(method = "tryHarvestBlock", at = @At("TAIL"))
    private void hookTryBreakBlockEnd(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Acquisition.stopBreaking();
    }
}

