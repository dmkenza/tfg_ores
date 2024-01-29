/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores.tfc;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import kenza.tfg_ores.TFGOres;
import net.dries007.tfc.common.blocks.DecorationBlockRegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.mixin.accessor.CreativeModeTabAccessor;
import net.dries007.tfc.util.SelfTests;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;


@SuppressWarnings("unused")
public final class KTFCCreativeTabs
{
//    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFGOres.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


//    public static final CreativeTabHolder EARTH = register("earth", () -> new ItemStack(TFCBlocks.ROCK_BLOCKS.get(Rock.QUARTZITE).get(Rock.BlockType.RAW).get()), TFCCreativeTabs::fillEarthTab);
    public static final CreativeTabHolder ORES = register("ores_tfg", () -> new ItemStack(KTFCItems.GRADED_ORES.get(KOre.ALUNITE).get(KOre.Grade.NORMAL).get()), KTFCCreativeTabs::fillOresTab);
//    public static final CreativeTabHolder ROCKS = register("rock", () -> new ItemStack(TFCBlocks.ROCK_BLOCKS.get(Rock.ANDESITE).get(Rock.BlockType.RAW).get()), TFCCreativeTabs::fillRocksTab);
//    public static final CreativeTabHolder METAL = register("metals_tfg", () -> new ItemStack(KTFCItems.METAL_ITEMS.get(KMetal.Default.LEAD).get(KMetal.ItemType.INGOT).get()), KTFCCreativeTabs::fillMetalTab);
//    public static final CreativeTabHolder WOOD = register("wood", () -> new ItemStack(TFCBlocks.WOODS.get(Wood.DOUGLAS_FIR).get(Wood.BlockType.LOG).get()), TFCCreativeTabs::fillWoodTab);
//    public static final CreativeTabHolder FOOD = register("food", () -> new ItemStack(TFCItems.FOOD.get(Food.RED_APPLE).get()), TFCCreativeTabs::fillFoodTab);
//    public static final CreativeTabHolder FLORA = register("flora", () -> new ItemStack(TFCBlocks.PLANTS.get(Plant.GOLDENROD).get()), TFCCreativeTabs::fillPlantsTab);
//    public static final CreativeTabHolder DECORATIONS = register("decorations", () -> new ItemStack(TFCBlocks.ALABASTER_BRICKS.get(DyeColor.CYAN).get()), TFCCreativeTabs::fillDecorationsTab);
//    public static final CreativeTabHolder MISC = register("misc", () -> new ItemStack(TFCItems.FIRESTARTER.get()), TFCCreativeTabs::fillMiscTab);


    private static CreativeTabHolder register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final RegistryObject<CreativeModeTab> reg = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("tfc.creative_tab." + name))
                .displayItems(displayItems)
                .build());
        return new CreativeTabHolder(reg, displayItems);
    }

//    public static Stream<CreativeModeTab.DisplayItemsGenerator> generators()
//    {
////        return Stream.of(EARTH, ORES, ROCKS, METAL, WOOD, FOOD, FLORA, DECORATIONS, MISC).map(holder -> holder.generator);
//        return Stream.of( ORES).map(holder -> holder.generator);
////        return Stream.of(ORES).map(holder -> holder.generator);
//    }

    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event)
    {
        final CreativeModeTabAccessor tab = (CreativeModeTabAccessor) event.getTab();
        final Supplier<ItemStack> prevIcon = tab.tfc$getIconGenerator();

        tab.tfc$setIconGenerator(() -> FoodCapability.setStackNonDecaying(prevIcon.get()));
        event.getEntries().forEach(e -> FoodCapability.setStackNonDecaying(e.getKey()));
    }

    private static void fillMetalTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        for (KMetal.Default metal : KMetal.Default.values())
        {
            for (KMetal.BlockType type : new KMetal.BlockType[] {
                    KMetal.BlockType.ANVIL,
                    KMetal.BlockType.BLOCK,
                    KMetal.BlockType.BLOCK_SLAB,
                    KMetal.BlockType.BLOCK_STAIRS,
                    KMetal.BlockType.BARS,
                    KMetal.BlockType.CHAIN,
                    KMetal.BlockType.TRAPDOOR,
                    KMetal.BlockType.LAMP,
            })
            {
                accept(out, KTFCBlocks.METALS, metal, type);
            }

            accept(out, KTFCItems.METAL_ITEMS, metal, KMetal.ItemType.UNFINISHED_LAMP);

//            if (metal == KMetal.Default.BRONZE)
//                accept(out, TFCBlocks.BRONZE_BELL);
//            else if (metal == KMetal.Default.BRASS)
//            {
//                accept(out, TFCBlocks.BRASS_BELL);
//                accept(out, TFCItems.BRASS_MECHANISMS);
//                accept(out, TFCItems.JACKS);
//            }
//            else if (metal == KMetal.Default.GOLD)
//                out.accept(Blocks.BELL);
//            else if (metal == KMetal.Default.RED_STEEL)
//                accept(out, TFCItems.RED_STEEL_BUCKET);
//            else if (metal == KMetal.Default.BLUE_STEEL)
//                accept(out, TFCItems.BLUE_STEEL_BUCKET);
//            else if (metal == KMetal.Default.WROUGHT_IRON)
//                accept(out, TFCItems.WROUGHT_IRON_GRILL);
//            else if (metal == KMetal.Default.STEEL)
//            {
//                accept(out, TFCBlocks.STEEL_PIPE);
//                accept(out, TFCBlocks.STEEL_PUMP);
//            }

            for (KMetal.ItemType itemType : new KMetal.ItemType[] {
                    KMetal.ItemType.INGOT,
                    KMetal.ItemType.DOUBLE_INGOT,
                    KMetal.ItemType.SHEET,
                    KMetal.ItemType.DOUBLE_SHEET,
                    KMetal.ItemType.ROD,

                    KMetal.ItemType.TUYERE,

                    KMetal.ItemType.PICKAXE,
                    KMetal.ItemType.PROPICK,
                    KMetal.ItemType.AXE,
                    KMetal.ItemType.SHOVEL,
                    KMetal.ItemType.HOE,
                    KMetal.ItemType.CHISEL,
                    KMetal.ItemType.HAMMER,
                    KMetal.ItemType.SAW,
                    KMetal.ItemType.KNIFE,
                    KMetal.ItemType.SCYTHE,
                    KMetal.ItemType.JAVELIN,
                    KMetal.ItemType.SWORD,
                    KMetal.ItemType.MACE,
                    KMetal.ItemType.FISHING_ROD,
                    KMetal.ItemType.SHEARS,

                    KMetal.ItemType.HELMET,
                    KMetal.ItemType.CHESTPLATE,
                    KMetal.ItemType.GREAVES,
                    KMetal.ItemType.BOOTS,

                    KMetal.ItemType.SHIELD,
                    KMetal.ItemType.HORSE_ARMOR,

                    KMetal.ItemType.PICKAXE_HEAD,
                    KMetal.ItemType.PROPICK_HEAD,
                    KMetal.ItemType.AXE_HEAD,
                    KMetal.ItemType.SHOVEL_HEAD,
                    KMetal.ItemType.HOE_HEAD,
                    KMetal.ItemType.CHISEL_HEAD,
                    KMetal.ItemType.HAMMER_HEAD,
                    KMetal.ItemType.SAW_BLADE,
                    KMetal.ItemType.KNIFE_BLADE,
                    KMetal.ItemType.SCYTHE_BLADE,
                    KMetal.ItemType.JAVELIN_HEAD,
                    KMetal.ItemType.SWORD_BLADE,
                    KMetal.ItemType.MACE_HEAD,
                    KMetal.ItemType.FISH_HOOK,

                    KMetal.ItemType.UNFINISHED_HELMET,
                    KMetal.ItemType.UNFINISHED_CHESTPLATE,
                    KMetal.ItemType.UNFINISHED_GREAVES,
                    KMetal.ItemType.UNFINISHED_BOOTS,
            })
            {
                accept(out, KTFCItems.METAL_ITEMS, metal, itemType);
            }
        }
    }



    private static void fillOresTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
//        accept(out, TFCItems.RAW_IRON_BLOOM);
//        accept(out, TFCItems.REFINED_IRON_BLOOM);
        for (KOre ore : KOre.values())
        {
            if (ore.isGraded())
            {
                accept(out, KTFCItems.GRADED_ORES, ore, KOre.Grade.POOR);
                accept(out, KTFCBlocks.SMALL_ORES, ore);
                accept(out, KTFCItems.GRADED_ORES, ore, KOre.Grade.NORMAL);
                accept(out, KTFCItems.GRADED_ORES, ore, KOre.Grade.RICH);
            }
        }
        for (KOre ore : KOre.values())
        {
            if (!ore.isGraded())
            {
                accept(out, KTFCItems.ORES, ore);
            }
        }
//        for (Gem gem : Gem.values())
//        {
//            accept(out, TFCItems.GEMS, gem);
//            accept(out, TFCItems.GEM_DUST, gem);
//        }
//        for (OreDeposit deposit : OreDeposit.values())
//        {
//            TFCBlocks.ORE_DEPOSITS.values().forEach(map -> accept(out, map, deposit));
//        }
        for (KOre ore : KOre.values())
        {
            if (ore.isGraded())
            {
                KTFCBlocks.GRADED_ORES.values().forEach(map -> map.get(ore).values().forEach(reg -> accept(out, reg)));
            }
            else
            {
                KTFCBlocks.ORES.values().forEach(map -> accept(out, map, ore));
            }
        }
    }


    private static <T extends ItemLike, R extends Supplier<T>, K1, K2> void accept(CreativeModeTab.Output out, Map<K1, Map<K2, R>> map, K1 key1, K2 key2)
    {
        if (map.containsKey(key1) && map.get(key1).containsKey(key2))
        {
            out.accept(map.get(key1).get(key2).get());
        }
    }

    private static <T extends ItemLike, R extends Supplier<T>, K> void accept(CreativeModeTab.Output out, Map<K, R> map, K key)
    {
        if (map.containsKey(key))
        {
            out.accept(map.get(key).get());
        }
    }

    private static <T extends ItemLike, R extends Supplier<T>> void accept(CreativeModeTab.Output out, R reg)
    {
        if (reg.get().asItem() == Items.AIR)
        {
            TerraFirmaCraft.LOGGER.error("BlockItem with no Item added to creative tab: " + reg);
            SelfTests.reportExternalError();
            return;
        }
        out.accept(reg.get());
    }

    private static void accept(CreativeModeTab.Output out, DecorationBlockRegistryObject decoration)
    {
        out.accept(decoration.stair().get());
        out.accept(decoration.slab().get());
        out.accept(decoration.wall().get());
    }



    private static <T> void consumeOurs(IForgeRegistry<T> registry, Consumer<T> consumer)
    {
        for (T value : registry)
        {
            if (Objects.requireNonNull(registry.getKey(value)).getNamespace().equals(MOD_ID))
            {
                consumer.accept(value);
            }
        }
    }

    public record CreativeTabHolder(RegistryObject<CreativeModeTab> tab, CreativeModeTab.DisplayItemsGenerator generator) {}
}