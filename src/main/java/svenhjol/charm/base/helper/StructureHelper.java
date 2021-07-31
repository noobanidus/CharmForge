package svenhjol.charm.base.helper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import svenhjol.charm.base.enums.ICharmEnum;
import svenhjol.charm.mixin.accessor.JigsawPatternAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public class StructureHelper {
    public static Map<ResourceLocation, JigsawPattern> vanillaPools = new HashMap<>();

    public static void addStructurePoolElement(ResourceLocation poolId, ResourceLocation pieceId, StructureProcessorList processor, JigsawPattern.PlacementBehaviour projection, int count, Registry<JigsawPattern> poolRegistry) {
        Pair<Function<JigsawPattern.PlacementBehaviour, LegacySingleJigsawPiece>, Integer> pair =
            Pair.of(JigsawPiece.func_242851_a(pieceId.toString(), processor), count);

        JigsawPiece element = pair.getFirst().apply(projection);
        JigsawPattern pool = poolRegistry.getOrDefault(poolId);

        // add custom piece to the element counts
        List<Pair<JigsawPiece, Integer>> listOfPieceEntries = new ArrayList<>(((JigsawPatternAccessor)pool).getRawTemplates());
        listOfPieceEntries.add(new Pair<>(element, count));
        ((JigsawPatternAccessor)pool).setRawTemplates(listOfPieceEntries);

        // add custom piece to the elements
        for (int i = 0; i < count; i++) {
            ((JigsawPatternAccessor)pool).getJigsawPieces().add(element);
        }
    }

    public static void addVillageHouse(VillageType type, ResourceLocation pieceId, int count, List<StructureProcessor> charmProcessor, Registry<JigsawPattern> poolRegistry) {
        ResourceLocation houses = new ResourceLocation("village/" + type.getString() + "/houses");
        JigsawPattern.PlacementBehaviour projection = JigsawPattern.PlacementBehaviour.RIGID;
        addStructurePoolElement(houses, pieceId, new StructureProcessorList(charmProcessor), projection, count, poolRegistry);
    }

    public enum VillageType implements ICharmEnum {
        DESERT,
        PLAINS,
        SAVANNA,
        SNOWY,
        TAIGA
    }
}
