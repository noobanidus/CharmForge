package svenhjol.charm.base.helper;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class BiomeHelper {
    public static Map<Biome.Category, List<RegistryKey<Biome>>> BIOME_CATEGORY_MAP = new HashMap<>();

    public static Biome getBiome(ServerWorld world, BlockPos pos) {
        BiomeManager biomeAccess = world.getBiomeManager();
        return biomeAccess.getBiome(pos);
    }

    public static Biome getBiomeFromBiomeKey(RegistryKey<Biome> biomeKey) {
        return WorldGenRegistries.BIOME.getValueForKey(biomeKey);
    }

    public static Optional<RegistryKey<Biome>> getBiomeKeyAtPosition(ServerWorld world, BlockPos pos) {
        return world.func_242406_i(pos);
    }

    public static BlockPos locateBiome(RegistryKey<Biome> biomeKey, ServerWorld world, BlockPos pos) {
        Biome biome = world.func_241828_r().getRegistry(Registry.BIOME_KEY).getValueForKey(biomeKey);
        return locateBiome(biome, world, pos);
    }

    public static BlockPos locateBiome(Biome biome, ServerWorld world, BlockPos pos) {
        return world.func_241116_a_(biome, pos, 6400, 8);
    }
}
