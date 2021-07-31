package svenhjol.charm.module;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.DecorationHandler;
import svenhjol.charm.base.helper.StructureHelper;
import svenhjol.charm.base.helper.VillagerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.village.LumberjackTradeOffers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static svenhjol.charm.base.helper.StructureHelper.addVillageHouse;

@Module(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.", hasSubscriptions = true)
public class Lumberjacks extends CharmModule {
    public static ResourceLocation VILLAGER_ID = new ResourceLocation(Charm.MOD_ID, "lumberjack");
    public static VillagerProfession LUMBERJACK;
    public static PointOfInterestType POIT;

    @Config(name = "Lumberjack house weight", description = "Chance of a custom building to spawn. For reference, a vanilla library is 5.")
    public static int buildingWeight = 5;

    public static void registerAfterWoodcutters() {
        POIT = VillagerHelper.addPointOfInterestType(Woodcutters.BLOCK_ID, Woodcutters.WOODCUTTER, 1);
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, SoundEvents.ENTITY_VILLAGER_WORK_MASON);
    }

    @Override
    public void init() { }


    @SubscribeEvent
    public void addBeekeeperHouses(FMLServerAboutToStartEvent event){
        StructureProcessorList vanillaProcessor = event.getServer().func_244267_aX().getRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOrDefault(new ResourceLocation("minecraft", "mossify_10_percent"));
        List<StructureProcessor> charmProcessor = new ArrayList<>(vanillaProcessor.func_242919_a());
        charmProcessor.addAll(DecorationHandler.SINGLE_POOL_ELEMENT_PROCESSORS);

        Registry<JigsawPattern> poolRegistry = event.getServer().func_244267_aX().getRegistry(Registry.JIGSAW_POOL_KEY);
        addVillageHouse(StructureHelper.VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_lumberjack_2"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.TAIGA, new ResourceLocation("charm:village/taiga/houses/taiga_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.SNOWY, new ResourceLocation("charm:village/snowy/houses/snowy_lumberjack_2"), buildingWeight, charmProcessor, poolRegistry);
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == LUMBERJACK) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            trades.put(1, Arrays.asList(
                new LumberjackTradeOffers.EmeraldsForOverworldLogs(),
                new LumberjackTradeOffers.CommonSaplingsForEmeralds(),
                new LumberjackTradeOffers.LaddersForEmeralds()
            ));
            trades.put(2, Arrays.asList(
                new LumberjackTradeOffers.EmeraldsForBones(),
                new LumberjackTradeOffers.BedForEmeralds(),
                new LumberjackTradeOffers.FencesForEmeralds()
            ));
            trades.put(3, Arrays.asList(
                new LumberjackTradeOffers.BarkForLogs(),
                new LumberjackTradeOffers.EmeraldsForStems(),
                new LumberjackTradeOffers.DoorsForEmeralds(),
                new LumberjackTradeOffers.UncommonSaplingsForEmeralds()
            ));
            trades.put(4, Arrays.asList(
                new LumberjackTradeOffers.CrateForEmeralds(),
                new LumberjackTradeOffers.MusicBlocksForLogs()
            ));
            trades.put(5, Arrays.asList(
                new LumberjackTradeOffers.BookcaseForEmeralds(),
                new LumberjackTradeOffers.WorkstationForEmeralds()
            ));
        }
    }

}
