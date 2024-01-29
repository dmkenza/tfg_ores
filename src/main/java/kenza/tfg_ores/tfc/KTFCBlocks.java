/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores.tfc;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.fluids.FluidId;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.IFluidLoggable;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistrationHelpers;

import static net.dries007.tfc.TerraFirmaCraft.*;

/**
 * Collection of all TFC blocks.
 * Unused is as the registry object fields themselves may be unused but they are required to register each item.
 * Whenever possible, avoid using hardcoded references to these, prefer tags or recipes.
 */
@SuppressWarnings("unused")
public final class KTFCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);


    // Ores

    public static final Map<Rock, Map<KOre, RegistryObject<Block>>> ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(KOre.class, ore -> !ore.isGraded(), ore ->
                    register(("ore/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
            )
    );
    public static final Map<Rock, Map<KOre, Map<KOre.Grade, RegistryObject<Block>>>> GRADED_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(KOre.class, KOre::isGraded, ore ->
                    Helpers.mapOfKeys(KOre.Grade.class, grade ->
                            register(("ore/" + grade.name() + "_" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
                    )
            )
    );
    public static final Map<KOre, RegistryObject<Block>> SMALL_ORES = Helpers.mapOfKeys(KOre.class, KOre::isGraded, type ->
            register(("ore/small_" + type.name()), () -> GroundcoverBlock.looseOre(Properties.of().mapColor(MapColor.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission().pushReaction(PushReaction.DESTROY)))
    );

    // Metals

    public static final Map<KMetal.Default, Map<KMetal.BlockType, RegistryObject<Block>>> METALS = Helpers.mapOfKeys(KMetal.Default.class, metal ->
            Helpers.mapOfKeys(KMetal.BlockType.class, type -> type.has(metal), type ->
                    register(type.createName(metal), type.create(metal), type.createBlockItem(new Item.Properties()))
            )
    );

    // Fluids

//    public static final Map<KMetal.Default, RegistryObject<LiquidBlock>> METAL_FLUIDS = Helpers.mapOfKeys(KMetal.Default.class, metal ->
//            registerNoItem("fluid/metal/" + metal.name(), () -> new LiquidBlock(KTFCFluids.METALS.get(metal).source(), Properties.copy(Blocks.LAVA).noLootTable()))
//    );



//    public static void editBlockRequiredTools()
//    {
//        for (Block block : new Block[] {
//            // All glass blocks are edited to require a tool (the gem saw), and loot tables that always drop themselves.
//            // We have to edit their 'required tool'-ness here
//            Blocks.GLASS,
//            Blocks.GLASS_PANE,
//            Blocks.TINTED_GLASS,
//            Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS,
//            Blocks.WHITE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.CYAN_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE,
//        })
//        {
//            // Need to do both the block settings and the block state since the value is copied there for every state
//            ((BlockBehaviourAccessor) block).getProperties().requiresCorrectToolForDrops();
//            for (BlockState state : block.getStateDefinition().getPossibleStates())
//            {
//                ((BlockStateBaseAccessor) state).setRequiresCorrectToolForDrops(true);
//            }
//        }
//    }


    public static ToIntFunction<BlockState> alwaysLit() {
        return s -> 15;
    }

    public static ToIntFunction<BlockState> lavaLoggedBlockEmission() {
        // This is resolved only at registration time, so we can't use the fast check (.getFluid() == Fluids.LAVA) and we have to use the slow check instead
        return state -> state.getValue(TFCBlockStateProperties.WATER_AND_LAVA).is(((IFluidLoggable) state.getBlock()).getFluidProperty().keyFor(Fluids.LAVA)) ? 15 : 0;
    }

    public static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static <B extends SignBlock> Map<Wood, Map<Metal.Default, RegistryObject<B>>> registerHangingSigns(String variant, BiFunction<ExtendedProperties, WoodType, B> factory) {
        return Helpers.mapOfKeys(Wood.class, wood ->
                Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasUtilities, metal -> register(
                        "wood/planks/" + variant + "/" + metal.getSerializedName() + "/" + wood.getSerializedName(),
                        () -> factory.apply(ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).noCollission().strength(1F).flammableLikePlanks().blockEntity(TFCBlockEntities.HANGING_SIGN).ticks(SignBlockEntity::tick), wood.getVanillaWoodType()),
                        (Function<B, BlockItem>) null)
                )
        );
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(TFCBlocks.BLOCKS, TFCItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}