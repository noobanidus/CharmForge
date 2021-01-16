package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.Acquisition;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    @Redirect(
            method = "itemInteractionForEntity",
            at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")
    )
    public void hook(List<ItemStack> drops, Consumer<? super ItemStack> action, ItemStack shears, PlayerEntity player) {
        if (!Acquisition.trySpawnToInventory(player, shears, drops)) {
            drops.forEach(action);
        }
    }
}
