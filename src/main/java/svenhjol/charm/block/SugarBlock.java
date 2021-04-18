package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmFallingBlock;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.ModHelper;
import svenhjol.charm.module.Bumblezone;

import java.util.HashSet;
import java.util.Set;

public class SugarBlock extends CharmFallingBlock {
    public SugarBlock(CharmModule module) {
        super(module, "sugar_block", Properties
            .create(Material.SAND)
            .sound(SoundType.SAND)
            .harvestTool(ToolType.SHOVEL)
            .hardnessAndResistance(0.5F)
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchWater(worldIn, pos, state)) {
            super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchWater(World world, BlockPos pos, BlockState state) {
        boolean waterBelow = false;

        for (Direction facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                BlockPos below = pos.offset(facing);
                if (world.getBlockState(below).getMaterial() == Material.WATER) {
                    waterBelow = true;
                    break;
                }
            }
        }

        if (waterBelow) {
            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
            world.removeBlock(pos, true);
        }

        if (waterBelow) {
            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));

            if (ModHelper.isLoaded("bumblezone") && ModuleHandler.enabled(Bumblezone.class)) {
                if (Bumblezone.bumblezoneFluid == null) {
                    Bumblezone.bumblezoneFluid = Registry.BLOCK.getOrDefault(Bumblezone.BUMBLEZONE_FLUID_ID);
                }

                world.setBlockState(pos, Bumblezone.bumblezoneFluid.getDefaultState(), 3);

                // Find all water blocks in contact recursively. Uses a set since we do not need duplicate positions
                Set<BlockPos> positionsToChange = Bumblezone.recursiveReplaceWater(world, pos, 0, 3, new HashSet<>());

                // Now change to sugar water after we found all water in range. Prevents weird shapes from being made when we delay this
                positionsToChange.forEach(waterPos -> world.setBlockState(waterPos, Bumblezone.bumblezoneFluid.getDefaultState(), 3));
            } else {
                world.removeBlock(pos, true);
            }
        }

        return waterBelow;
    }
}
