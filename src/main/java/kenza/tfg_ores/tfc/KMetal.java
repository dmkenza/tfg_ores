/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores.tfc;

import com.google.gson.JsonObject;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.TFCChainBlock;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.items.*;
import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class KMetal
{
    public static final ResourceLocation UNKNOWN_ID = Helpers.identifier("unknown");
    public static final ResourceLocation WROUGHT_IRON_ID = Helpers.identifier("wrought_iron");

    public static final DataManager<KMetal> MANAGER = new DataManager<>(Helpers.identifier("metals"), "metal", KMetal::fromJson, KMetal::fromNetwork, KMetal::encode, Packet::new);

    private static final Map<Fluid, KMetal> METAL_FLUIDS = new HashMap<>();

    /**
     * Reverse lookup for metals attached to fluids.
     * For the other direction, see {@link KMetal#getFluid()}.
     *
     * @param fluid The fluid, can be empty.
     * @return A metal if it exists, and null if it doesn't.
     */
    @Nullable
    public static KMetal get(Fluid fluid)
    {
        return METAL_FLUIDS.get(fluid);
    }

    /**
     * Get the 'unknown' metal. This is the only metal that any assurances are made that it exists.
     */
    public static KMetal unknown()
    {
        return MANAGER.getOrThrow(UNKNOWN_ID);
    }

    /**
     * @return The matching metal for a given ingot, as defined by the metal itself.
     */
    @Nullable
    public static KMetal getFromIngot(ItemStack stack)
    {
        for (KMetal metal : MANAGER.getValues())
        {
            if (metal.isIngot(stack) || metal.isDoubleIngot(stack))
            {
                return metal;
            }
        }
        return null;
    }

    @Nullable
    public static KMetal getFromSheet(ItemStack stack)
    {
        for (KMetal metal : MANAGER.getValues())
        {
            if (metal.isSheet(stack))
            {
                return metal;
            }
        }
        return null;
    }

    public static void updateMetalFluidMap()
    {
        // Ensure 'unknown' metal exists
        unknown();

        // Reload fluid -> metal map
        METAL_FLUIDS.clear();
        for (KMetal metal : MANAGER.getValues())
        {
            METAL_FLUIDS.put(metal.getFluid(), metal);
        }
    }

    private static KMetal fromJson(ResourceLocation id, JsonObject json)
    {
        final int tier = JsonHelpers.getAsInt(json, "tier", 0);
        final Fluid fluid = JsonHelpers.getRegistryEntry(json, "fluid", ForgeRegistries.FLUIDS);
        final float specificHeatCapacity = JsonHelpers.getAsFloat(json, "specific_heat_capacity");
        final float meltTemperature = JsonHelpers.getAsFloat(json, "melt_temperature");

        final Ingredient ingots = json.has("ingots") ? Ingredient.fromJson(JsonHelpers.get(json, "ingots")) : null;
        final Ingredient doubleIngots = json.has("double_ingots") ? Ingredient.fromJson(JsonHelpers.get(json, "double_ingots")) : null;
        final Ingredient sheets = json.has("sheets") ? Ingredient.fromJson(JsonHelpers.get(json, "sheets")) : null;

        return new KMetal(id, tier, fluid, meltTemperature, specificHeatCapacity, ingots, doubleIngots, sheets);
    }

    private static KMetal fromNetwork(ResourceLocation id, FriendlyByteBuf buffer)
    {
        final int tier = buffer.readVarInt();
        final Fluid fluid = buffer.readRegistryIdUnsafe(ForgeRegistries.FLUIDS);
        final float meltTemperature = buffer.readFloat();
        final float specificHeatCapacity = buffer.readFloat();

        final Ingredient ingots = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);
        final Ingredient doubleIngots = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);
        final Ingredient sheets = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);

        return new KMetal(id, tier, fluid, meltTemperature, specificHeatCapacity, ingots, doubleIngots, sheets);
    }

    private final int tier;
    private final Fluid fluid;
    private final float meltTemperature;
    private final float specificHeatCapacity;

    private final ResourceLocation id;
    private final ResourceLocation textureId;
    private final ResourceLocation softTextureId;
    private final String translationKey;

    @Nullable
    private final Ingredient ingots, doubleIngots, sheets;

    /**
     * <strong>Not for general purpose use!</strong> Explicitly creates unregistered metals outside the system, which are able to act as rendering stubs.
     */
    public KMetal(ResourceLocation id)
    {
        this(id, 0, Fluids.EMPTY, 0, 0, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
    }

    private KMetal(ResourceLocation id, int tier, Fluid fluid, float meltTemperature, float specificHeatCapacity, @Nullable Ingredient ingots, @Nullable Ingredient doubleIngots, @Nullable Ingredient sheets)
    {
        this.id = id;
        this.textureId = new ResourceLocation(id.getNamespace(), "block/metal/block/" + id.getPath());
        this.softTextureId = new ResourceLocation(id.getNamespace(), "block/metal/smooth/" + id.getPath());

        this.tier = tier;
        this.fluid = fluid;
        this.meltTemperature = meltTemperature;
        this.specificHeatCapacity = specificHeatCapacity;
        this.translationKey = "metal." + id.getNamespace() + "." + id.getPath();

        this.ingots = ingots;
        this.doubleIngots = doubleIngots;
        this.sheets = sheets;
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(tier);
        buffer.writeRegistryIdUnsafe(ForgeRegistries.FLUIDS, fluid);
        buffer.writeFloat(meltTemperature);
        buffer.writeFloat(specificHeatCapacity);

        Helpers.encodeNullable(ingots, buffer, Ingredient::toNetwork);
        Helpers.encodeNullable(doubleIngots, buffer, Ingredient::toNetwork);
        Helpers.encodeNullable(sheets, buffer, Ingredient::toNetwork);
    }

    public ResourceLocation getId()
    {
        return id;
    }

    public ResourceLocation getTextureId()
    {
        return textureId;
    }

    public ResourceLocation getSoftTextureId()
    {
        return softTextureId;
    }

    public int getTier()
    {
        return tier;
    }

    public Fluid getFluid()
    {
        return fluid;
    }

    public float getMeltTemperature()
    {
        return meltTemperature;
    }

    /**
     * @return The Specific Heat Capacity of the metal. Units of Energy / °C
     * @see IHeat#getHeatCapacity()
     */
    public float getHeatCapacity(float mB)
    {
        return getSpecificHeatCapacity() * mB;
    }

    /**
     * @return The Specific Heat Capacity of the metal. Units of Energy / (°C * mB)
     */
    public float getSpecificHeatCapacity()
    {
        return specificHeatCapacity;
    }

    public MutableComponent getDisplayName()
    {
        return Component.translatable(translationKey);
    }

    public String getTranslationKey()
    {
        return translationKey;
    }

    public boolean isIngot(ItemStack stack)
    {
        return ingots != null && ingots.test(stack);
    }

    public boolean isDoubleIngot(ItemStack stack)
    {
        return doubleIngots != null && doubleIngots.test(stack);
    }

    public boolean isSheet(ItemStack stack)
    {
        return sheets != null && sheets.test(stack);
    }

    @Nullable
    @VisibleForTesting
    public Ingredient getIngotIngredient()
    {
        return ingots;
    }

    @Nullable
    @VisibleForTesting
    public Ingredient getDoubleIngotIngredient()
    {
        return doubleIngots;
    }

    @Nullable
    @VisibleForTesting
    public Ingredient getSheetIngredient()
    {
        return sheets;
    }

    /**
     * Metals / Anvils:
     * T0 - Rock - Work None, Weld T1
     * T1 - Copper - Work T1, Weld T2
     * T2 - Bronze / Bismuth Bronze / Black Bronze - Work T2, Weld T3
     * T3 - Wrought Iron - Work T3, Weld T4
     * T4 - Steel - Work T4, Weld T5
     * T5 - Black Steel - Work T5, Weld T6
     * T6 - Red Steel / Blue Steel - Work T6, Weld T6
     */
    public enum Tier
    {
        TIER_0, TIER_I, TIER_II, TIER_III, TIER_IV, TIER_V, TIER_VI;

        private static final Tier[] VALUES = values();

        public static Tier valueOf(int tier)
        {
            return tier < 0 || tier > VALUES.length ? TIER_I : VALUES[tier];
        }

        public Tier next()
        {
            return this == TIER_VI ? TIER_VI : VALUES[this.ordinal() + 1];
        }

        public Tier previous()
        {
            return this == TIER_0 ? TIER_0 : VALUES[this.ordinal() - 1];
        }

        public Component getDisplayName()
        {
            return Helpers.translateEnum(this);
        }
    }

    /**
     * Default metals that are used for block registration calls.
     * Not extensible.
     *
     * @see KMetal instead and register via json
     */
    public enum Default implements StringRepresentable, RegistryMetal
    {
        LEAD(0xFF5e5d6e, MapColor.TERRACOTTA_BLUE, Rarity.RARE, true, false, false);

        private final String serializedName;
        private final boolean parts, armor, utility;
        private final net.dries007.tfc.util.Metal.Tier metalTier;
        @Nullable private final net.minecraft.world.item.Tier toolTier;
        @Nullable private final ArmorMaterial armorTier;
        private final MapColor mapColor;
        private final Rarity rarity;
        private final int color;

        Default(int color, MapColor mapColor, Rarity rarity, boolean parts, boolean armor, boolean utility)
        {
            this(color, mapColor, rarity,  net.dries007.tfc.util.Metal.Tier.TIER_0, null, null, parts, armor, utility);
        }

        Default(int color, MapColor mapColor, Rarity rarity,  net.dries007.tfc.util.Metal.Tier metalTier, @Nullable net.minecraft.world.item.Tier toolTier, @Nullable ArmorMaterial armorTier, boolean parts, boolean armor, boolean utility)
        {
            this.serializedName = name().toLowerCase(Locale.ROOT);
            this.metalTier = metalTier;
            this.toolTier = toolTier;
            this.armorTier = armorTier;
            this.rarity = rarity;
            this.mapColor = mapColor;
            this.color = color;

            this.parts = parts;
            this.armor = armor;
            this.utility = utility;
        }

        @Override
        public String getSerializedName()
        {
            return serializedName;
        }

        public int getColor()
        {
            return color;
        }

        public Rarity getRarity()
        {
            return rarity;
        }

        public boolean hasParts()
        {
            return parts;
        }

        public boolean hasArmor()
        {
            return armor;
        }

        public boolean hasTools()
        {
            return toolTier != null;
        }

        public boolean hasUtilities()
        {
            return utility;
        }

        @Override
        public net.minecraft.world.item.Tier toolTier()
        {
            return Objects.requireNonNull(toolTier, "Tried to get non-existent tier from " + name());
        }

        @Override
        public ArmorMaterial armorTier()
        {
            return Objects.requireNonNull(armorTier, "Tried to get non-existent armor tier from " + name());
        }

        @Override
        public net.dries007.tfc.util.Metal.Tier metalTier()
        {
            return metalTier;
        }

        @Override
        public MapColor mapColor()
        {
            return mapColor;
        }

        @Override
        public Supplier<Block> getFullBlock()
        {
            return KTFCBlocks.METALS.get(this).get(BlockType.BLOCK);
        }
    }

    public enum BlockType
    {
        ANVIL(Type.UTILITY, metal -> new AnvilBlock(ExtendedProperties.of().mapColor(metal.mapColor()).noOcclusion().sound(SoundType.METAL).strength(10, 10).requiresCorrectToolForDrops().blockEntity(TFCBlockEntities.ANVIL), metal.metalTier())),
        BLOCK(Type.PART, metal -> new Block(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BLOCK_SLAB(Type.PART, metal -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BLOCK_STAIRS(Type.PART, metal -> new StairBlock(() -> metal.getFullBlock().get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(metal.mapColor()).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))),
        BARS(Type.UTILITY, metal -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(metal.mapColor()).requiresCorrectToolForDrops().strength(6.0F, 7.0F).sound(SoundType.METAL).noOcclusion())),
        CHAIN(Type.UTILITY, metal -> new TFCChainBlock(Block.Properties.of().mapColor(metal.mapColor()).requiresCorrectToolForDrops().strength(5, 6).sound(SoundType.CHAIN).lightLevel(TFCBlocks.lavaLoggedBlockEmission()))),
        LAMP(Type.UTILITY, metal -> new LampBlock(ExtendedProperties.of().mapColor(metal.mapColor()).noOcclusion().sound(SoundType.LANTERN).strength(4, 10).randomTicks().pushReaction(PushReaction.DESTROY).lightLevel(state -> state.getValue(LampBlock.LIT) ? 15 : 0).blockEntity(TFCBlockEntities.LAMP)), (block, properties) -> new LampBlockItem(block, properties.stacksTo(1))),
        TRAPDOOR(Type.UTILITY, metal -> new TrapDoorBlock(Block.Properties.of().mapColor(metal.mapColor()).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().isValidSpawn(TFCBlocks::never), BlockSetType.IRON));

        private final Function<RegistryMetal, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final Type type;
        private final String serializedName;

        BlockType(Type type, Function<RegistryMetal, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this.type = type;
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        BlockType(Type type, Function<RegistryMetal, Block> blockFactory)
        {
            this(type, blockFactory, BlockItem::new);
        }

        public Supplier<Block> create(RegistryMetal metal)
        {
            return () -> blockFactory.apply(metal);
        }

        public Function<Block, BlockItem> createBlockItem(Item.Properties properties)
        {
            return block -> blockItemFactory.apply(block, properties);
        }

        public boolean has(Default metal)
        {
            return type.hasType(metal);
        }

        public String createName(RegistryMetal metal)
        {
            if (this == BLOCK_SLAB || this == BLOCK_STAIRS)
            {
                return BLOCK.createName(metal) + (this == BLOCK_SLAB ? "_slab" : "_stairs");
            }
            else
            {
                return "metal/" + serializedName + "/" + metal.getSerializedName();
            }
        }
    }

    public enum ItemType
    {
        // Generic
        INGOT(Type.DEFAULT, true, metal -> new IngotItem(properties(metal))),
        DOUBLE_INGOT(Type.PART, false),
        SHEET(Type.PART, false),
        DOUBLE_SHEET(Type.PART, false),
        ROD(Type.PART, false),
        TUYERE(Type.TOOL, metal -> new TieredItem(metal.toolTier(), properties(metal))),
        FISH_HOOK(Type.TOOL, false),
        FISHING_ROD(Type.TOOL, metal -> new TFCFishingRodItem(properties(metal).defaultDurability(metal.toolTier().getUses()), metal.toolTier())),
        UNFINISHED_LAMP(Type.UTILITY, metal -> new Item(properties(metal))),

        // Tools and Tool Heads
        PICKAXE(Type.TOOL, metal -> new PickaxeItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.75f, metal.toolTier()), -2.8F, properties(metal))),
        PICKAXE_HEAD(Type.TOOL, true),
        PROPICK(Type.TOOL, metal -> new PropickItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.5f, metal.toolTier()), -2.8F, properties(metal))),
        PROPICK_HEAD(Type.TOOL, true),
        AXE(Type.TOOL, metal -> new AxeItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(1.5f, metal.toolTier()), -3.1F, properties(metal))),
        AXE_HEAD(Type.TOOL, true),
        SHOVEL(Type.TOOL, metal -> new ShovelItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.875F, metal.toolTier()), -3.0F, properties(metal))),
        SHOVEL_HEAD(Type.TOOL, true),
        HOE(Type.TOOL, metal -> new TFCHoeItem(metal.toolTier(), -1, -2f, properties(metal))),
        HOE_HEAD(Type.TOOL, true),
        CHISEL(Type.TOOL, metal -> new ChiselItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.27f, metal.toolTier()), -1.5F, properties(metal))),
        CHISEL_HEAD(Type.TOOL, true),
        HAMMER(Type.TOOL, metal -> new HammerItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(1f, metal.toolTier()), -3, properties(metal), metal.getSerializedName())),
        HAMMER_HEAD(Type.TOOL, true),
        SAW(Type.TOOL, metal -> new AxeItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.5f, metal.toolTier()), -3, properties(metal))),
        SAW_BLADE(Type.TOOL, true),
        JAVELIN(Type.TOOL, metal -> new JavelinItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.7f, metal.toolTier()), 1.5f * metal.toolTier().getAttackDamageBonus(), -2.6F, properties(metal), metal.getSerializedName())),
        JAVELIN_HEAD(Type.TOOL, true),
        SWORD(Type.TOOL, metal -> new SwordItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(1f, metal.toolTier()), -2.4F, properties(metal))),
        SWORD_BLADE(Type.TOOL, true),
        MACE(Type.TOOL, metal -> new MaceItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(1.3f, metal.toolTier()), -3, properties(metal))),
        MACE_HEAD(Type.TOOL, true),
        KNIFE(Type.TOOL, metal -> new ToolItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.6f, metal.toolTier()), -2.0F, TFCTags.Blocks.MINEABLE_WITH_KNIFE, properties(metal))),
        KNIFE_BLADE(Type.TOOL, true),
        SCYTHE(Type.TOOL, metal -> new ScytheItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.7f, metal.toolTier()), -3.2F, TFCTags.Blocks.MINEABLE_WITH_SCYTHE, properties(metal))),
        SCYTHE_BLADE(Type.TOOL, true),
        SHEARS(Type.TOOL, metal -> new ShearsItem(properties(metal).defaultDurability(metal.toolTier().getUses()))),

        // Armor
        UNFINISHED_HELMET(Type.ARMOR, false),
        HELMET(Type.ARMOR, metal -> new ArmorItem(metal.armorTier(), ArmorItem.Type.HELMET, properties(metal))),
        UNFINISHED_CHESTPLATE(Type.ARMOR, false),
        CHESTPLATE(Type.ARMOR, metal -> new ArmorItem(metal.armorTier(), ArmorItem.Type.CHESTPLATE, properties(metal))),
        UNFINISHED_GREAVES(Type.ARMOR, false),
        GREAVES(Type.ARMOR, metal -> new ArmorItem(metal.armorTier(), ArmorItem.Type.LEGGINGS, properties(metal))),
        UNFINISHED_BOOTS(Type.ARMOR, false),
        BOOTS(Type.ARMOR, metal -> new ArmorItem(metal.armorTier(), ArmorItem.Type.BOOTS, properties(metal))),
        HORSE_ARMOR(Type.ARMOR, metal -> new HorseArmorItem(Mth.floor(metal.armorTier().getDefenseForType(ArmorItem.Type.CHESTPLATE) * 1.5), Helpers.identifier("textures/entity/animal/horse_armor/" + metal.getSerializedName() + ".png"), properties(metal))),

        SHIELD(Type.TOOL, metal -> new TFCShieldItem(metal.toolTier(), properties(metal)));

        public static Item.Properties properties(RegistryMetal metal)
        {
            return new Item.Properties().rarity(metal.getRarity());
        }

        private final Function<RegistryMetal, Item> itemFactory;
        private final Type type;
        private final boolean mold;

        ItemType(Type type, boolean mold)
        {
            this(type, mold, metal -> new Item(properties(metal)));
        }

        ItemType(Type type, Function<RegistryMetal, Item> itemFactory)
        {
            this(type, false, itemFactory);
        }

        ItemType(Type type, boolean mold, Function<RegistryMetal, Item> itemFactory)
        {
            this.type = type;
            this.mold = mold;
            this.itemFactory = itemFactory;
        }

        public Item create(RegistryMetal metal)
        {
            return itemFactory.apply(metal);
        }

        public boolean has(Default metal)
        {
            return type.hasType(metal);
        }

        public boolean hasMold()
        {
            return mold;
        }
    }

    private enum Type
    {
        DEFAULT(metal -> true),
        PART(Default::hasParts),
        TOOL(Default::hasTools),
        ARMOR(Default::hasArmor),
        UTILITY(Default::hasUtilities);

        private final Predicate<Default> predicate;

        Type(Predicate<Default> predicate)
        {
            this.predicate = predicate;
        }

        boolean hasType(Default metal)
        {
            return predicate.test(metal);
        }
    }

    public static class Packet extends DataManagerSyncPacket<KMetal> {}
}