package svenhjol.charm.module;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.BookcaseBlock;
import svenhjol.charm.client.BookcasesClient;
import svenhjol.charm.container.BookcaseContainer;
import svenhjol.charm.tileentity.BookcaseTileEntity;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = BookcasesClient.class, description = "Bookshelves that can hold up to 9 stacks of books and maps.")
public class Bookcases extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "bookcase");
    public static final Map<IVariantMaterial, BookcaseBlock> BOOKCASE_BLOCKS = new HashMap<>();

    public static ContainerType<BookcaseContainer> CONTAINER;
    public static TileEntityType<BookcaseTileEntity> TILE_ENTITY;

    public static List<Item> validItems = new ArrayList<>();

    @Config(name = "Valid books", description = "Additional items that may be placed in bookcases.")
    public static List<String> configValidItems = new ArrayList<>();

    @Override
    public void register() {
        VanillaVariantMaterial.getTypes().forEach(type -> {
            BOOKCASE_BLOCKS.put(type, new BookcaseBlock(this, type));
        });

        CONTAINER = RegistryHandler.container(ID, BookcaseContainer::new);
        TILE_ENTITY = RegistryHandler.tileEntity(ID, BookcaseTileEntity::new);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        validItems.addAll(Arrays.asList(
                Items.BOOK,
                Items.ENCHANTED_BOOK,
                Items.WRITTEN_BOOK,
                Items.WRITABLE_BOOK,
                Items.KNOWLEDGE_BOOK,
                Items.PAPER,
                Items.MAP,
                Items.FILLED_MAP,
                Atlas.ATLAS_ITEM
        ));
        configValidItems.forEach(string -> {
            Item item = Registry.ITEM.getOrDefault(new ResourceLocation(string));
            validItems.add(item);
        });
    }

    public static boolean canContainItem(ItemStack stack) {
        return validItems.contains(stack.getItem());
    }
}
