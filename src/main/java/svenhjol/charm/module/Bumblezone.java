package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

import java.util.HashSet;
import java.util.Set;

@Module(mod = Charm.MOD_ID, description = "Bumblezone integration.")
public class Bumblezone extends CharmModule {
    public static final ResourceLocation BUMBLEZONE_FLUID_ID = new ResourceLocation("the_bumblezone", "sugar_water_block");
    public static Block bumblezoneFluid = null;

    /**
     * Will recursively track down and replace touching water blocks with bumblezone fluid.
     * Do not set the maxDepth too high!
     * @return - waterPos
     */
    public static Set<BlockPos> recursiveReplaceWater(World world, BlockPos position, int depth, int maxDepth, HashSet<BlockPos> waterPos){
        // exit when we hit as far as we wanted
        if(depth == maxDepth) return waterPos;

        // Find the touching water blocks, replace them, and call this method on those blocks
        BlockPos.Mutable neighborPos = new BlockPos.Mutable();
        for (Direction facing : Direction.values()) {
            neighborPos.setPos(position).move(facing);
            BlockState neighborBlock = world.getBlockState(neighborPos);

            // Found watery block to replace, store the position of the water
            if (!neighborBlock.getBlock().matchesBlock(bumblezoneFluid) && neighborBlock.getMaterial() == Material.WATER) {
                waterPos.add(neighborPos.toImmutable());
                recursiveReplaceWater(world, neighborPos, depth + 1, maxDepth, waterPos);
            }
        }

        return waterPos;
    }
}
