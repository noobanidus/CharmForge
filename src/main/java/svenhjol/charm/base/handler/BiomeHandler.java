package svenhjol.charm.base.handler;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import svenhjol.charm.base.helper.BiomeHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class BiomeHandler {
    public static void init() {
        WorldGenRegistries.BIOME.getEntries().forEach(entry -> {
            Biome.Category category = entry.getValue().getCategory();
            RegistryKey<Biome> key = entry.getKey();

            BiomeHelper.BIOME_CATEGORY_MAP.putIfAbsent(category, new ArrayList<>());
            BiomeHelper.BIOME_CATEGORY_MAP.get(category).add(key);
        });
    }
}
