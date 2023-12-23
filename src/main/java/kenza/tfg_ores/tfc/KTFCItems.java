/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores.tfc;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.dries007.tfc.common.TFCTiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.mixin.accessor.ItemAccessor;
import net.dries007.tfc.util.Helpers;

import static net.dries007.tfc.TerraFirmaCraft.*;

/**
 * Collection of all TFC items.
 * Organized by {@link TFCCreativeTabs}
 * Unused is as the registry object fields themselves may be unused, but they are required to register each item.
 * Whenever possible, avoid using hardcoded references to these, prefer tags or recipes.
 */
@SuppressWarnings("unused")
public final class KTFCItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    // Ores

    public static final Map<KOre, RegistryObject<Item>> ORES = Helpers.mapOfKeys(KOre.class, ore -> !ore.isGraded(), type ->
        register("ore/" + type.name())
    );
    public static final Map<KOre, Map<KOre.Grade, RegistryObject<Item>>> GRADED_ORES = Helpers.mapOfKeys(KOre.class, KOre::isGraded, ore ->
        Helpers.mapOfKeys(KOre.Grade.class, grade ->
            register("ore/" + grade.name() + '_' + ore.name())
        )
    );

    public static void editItemMaxDamage()
    {
        ((ItemAccessor) Items.FLINT_AND_STEEL).accessor$setMaxDamage(TFCTiers.STEEL.getUses());
    }

    private static <T extends EntityType<? extends Mob>> RegistryObject<Item> registerSpawnEgg(RegistryObject<T> entity, int color1, int color2)
    {
        return register("spawn_egg/" + entity.getId().getPath(), () -> new ForgeSpawnEggItem(entity, color1, color2, new Item.Properties()));
    }

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}