package svenhjol.charm.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.GravityStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GravityStructureProcessor.class)
public class GravityStructureProcessorMixin {
    private static final ThreadLocal<Biome.Category> biomeCategory = new ThreadLocal<>();

    @Inject(
        method = "func_230386_a_",
        at = @At("HEAD")
    )
    private void hookProcess(IWorldReader worldView, BlockPos pos, BlockPos p_230386_3_, Template.BlockInfo p_230386_4_, Template.BlockInfo p_230386_5_, PlacementSettings p_230386_6_, CallbackInfoReturnable<Template.BlockInfo> cir) {
        biomeCategory.set(worldView.getBiome(pos).getCategory());
    }

    @ModifyArg(
        method = "func_230386_a_",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/IWorldReader;getHeight(Lnet/minecraft/world/gen/Heightmap$Type;II)I"
        ),
        index = 0
    )
    private Heightmap.Type hookProcess(Heightmap.Type in) {
        Biome.Category category = biomeCategory.get();
        if (category == Biome.Category.OCEAN && in == Heightmap.Type.WORLD_SURFACE_WG) {
            return Heightmap.Type.OCEAN_FLOOR_WG;
        }

        return in;
    }
}
