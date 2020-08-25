package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(description = "Lightweight armor is invisible and does not increase mob awareness when drinking Potion of Invisibility.")
public class ArmorInvisibility extends MesonModule {

    public static List<Item> invisibleItems = new ArrayList<>();

    @Override
    public void init() {
        if (!enabled)
            return;

        invisibleItems.addAll(Arrays.asList(
            Items.LEATHER_HELMET,
            Items.LEATHER_CHESTPLATE,
            Items.LEATHER_LEGGINGS,
            Items.LEATHER_BOOTS,
            Items.CHAINMAIL_HELMET,
            Items.CHAINMAIL_CHESTPLATE,
            Items.CHAINMAIL_LEGGINGS,
            Items.CHAINMAIL_BOOTS
        ));
    }

    public static boolean shouldArmorBeInvisible(Entity entity, ItemStack stack) {
        if (entity instanceof LivingEntity) {
            if (((LivingEntity)entity).getStatusEffect(StatusEffects.INVISIBILITY) != null)
                return invisibleItems.contains(stack.getItem());
        }

        return false;
    }
}
