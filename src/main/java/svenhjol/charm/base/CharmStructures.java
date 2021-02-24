package svenhjol.charm.base;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.DecorationHandler;
import svenhjol.charm.base.structure.DataBlockProcessor;

import java.util.Arrays;

public class CharmStructures {
    private static final DataBlockProcessor DATA_BLOCK_PROCESSOR = new DataBlockProcessor();
    private static final Codec<DataBlockProcessor> CODEC = Codec.unit(DATA_BLOCK_PROCESSOR);
    public static final IStructureProcessorType<DataBlockProcessor> DATA_BLOCK_PROCESSOR_TYPE = () -> CODEC;

    public static void init() {
        DecorationHandler.SINGLE_POOL_ELEMENT_PROCESSORS.addAll(Arrays.asList(
            new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.GRAY_STAINED_GLASS)),
            DATA_BLOCK_PROCESSOR
        ));
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Charm.MOD_ID, "data_block_processor"), DATA_BLOCK_PROCESSOR_TYPE);
    }
}
