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
import svenhjol.charm.base.helper.ModHelper;
import svenhjol.charm.base.helper.StructureHelper;
import svenhjol.charm.base.helper.VillagerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.mixin.accessor.PointOfInterestTypeAccessor;
import svenhjol.charm.village.BeekeeperTradeOffers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static svenhjol.charm.base.helper.StructureHelper.addVillageHouse;

@Module(mod = Charm.MOD_ID, description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.", hasSubscriptions = true)
public class Beekeepers extends CharmModule {
    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "beekeeper");
    public static VillagerProfession BEEKEEPER;

    @Config(name = "Beekeeper house weight", description = "Chance of a custom building to spawn. For reference, a vanilla library is 5.")
    public static int buildingWeight = 5;

    @Config(name = "Override", description = "This module is automatically disabled if Buzzier Bees is present. Set true to force enable.")
    public static boolean override = false;

    @Override
    public void register() {
        BEEKEEPER = VillagerHelper.addProfession(ID, PointOfInterestType.BEEHIVE, SoundEvents.BLOCK_BEEHIVE_WORK);
    }

    @Override
    public boolean depends() {
        return !ModHelper.isLoaded("buzzier_bees") || override;
    }

    @Override
    public void init() {
        // HACK: set ticketCount so that villager can use it as job site
        ((PointOfInterestTypeAccessor)PointOfInterestType.BEEHIVE).setMaxFreeTickets(1);

    }

    @SubscribeEvent
    public void addBeekeeperHouses(FMLServerAboutToStartEvent event){
        StructureProcessorList vanillaProcessor = event.getServer().func_244267_aX().getRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOrDefault(new ResourceLocation("minecraft", "mossify_10_percent"));
        List<StructureProcessor> charmProcessor = new ArrayList<>(vanillaProcessor.func_242919_a());
        charmProcessor.addAll(DecorationHandler.SINGLE_POOL_ELEMENT_PROCESSORS);

        Registry<JigsawPattern> poolRegistry = event.getServer().func_244267_aX().getRegistry(Registry.JIGSAW_POOL_KEY);
        addVillageHouse(StructureHelper.VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_beejack"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_beekeeper_2"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.TAIGA, new ResourceLocation("charm:village/taiga/houses/taiga_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
        addVillageHouse(StructureHelper.VillageType.SNOWY, new ResourceLocation("charm:village/snowy/houses/snowy_lumberbee_1"), buildingWeight, charmProcessor, poolRegistry);
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == BEEKEEPER) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            trades.put(1, Arrays.asList(
                new BeekeeperTradeOffers.EmeraldsForFlowers(),
                new BeekeeperTradeOffers.BottlesForEmerald()
            ));
            trades.put(2, Arrays.asList(
                new BeekeeperTradeOffers.EmeraldsForCharcoal(),
                new BeekeeperTradeOffers.BeeswaxForEmeralds()
            ));
            trades.put(3, Arrays.asList(
                new BeekeeperTradeOffers.EmeraldsForHoneycomb(),
                new BeekeeperTradeOffers.CampfireForEmerald()
            ));
            trades.put(4, Arrays.asList(
                new BeekeeperTradeOffers.LeadForEmeralds()
            ));
            trades.put(5, Arrays.asList(
                new BeekeeperTradeOffers.PopulatedBeehiveForEmeralds()
            ));
        }
    }
}
