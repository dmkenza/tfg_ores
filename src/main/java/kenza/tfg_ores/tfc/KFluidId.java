/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores.tfc;

import net.dries007.tfc.common.fluids.Alcohol;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Merged enum
public record KFluidId(String name, OptionalInt color, Supplier<? extends Fluid> fluid)
{

    private static final Map<Enum<?>, KFluidId> IDENTITY = new HashMap<>();
    private static final List<KFluidId> VALUES = Stream.of(
//            Stream.of(SALT_WATER, SPRING_WATER),
//            Arrays.stream(SimpleFluid.values()).map(fluid -> fromEnum(fluid, fluid.getColor(), fluid.getId(), TFCFluids.SIMPLE_FLUIDS.get(fluid).source())),
//            Arrays.stream(Alcohol.values()).map(fluid -> fromEnum(fluid, fluid.getColor(), fluid.getId(), TFCFluids.ALCOHOLS.get(fluid).source())),
//            Arrays.stream(DyeColor.values()).map(dye -> fromEnum(dye, TFCFluids.dyeColorToInt(dye), dye.getSerializedName() + "_dye", TFCFluids.COLORED_FLUIDS.get(dye).source())),
            Arrays.stream(KMetal.Default.values()).map(metal -> fromEnum(metal, metal.getColor(), "metal/" + metal.getSerializedName(), KTFCFluids.METALS.get(metal).source()))
        )
        .flatMap(Function.identity())
        .toList();

    public static <R> Map<KFluidId, R> mapOf(Function<? super KFluidId, ? extends R> map)
    {
        return VALUES.stream().collect(Collectors.toMap(Function.identity(), map));
    }

    public static KFluidId asType(Enum<?> identity)
    {
        return IDENTITY.get(identity);
    }

    private static KFluidId fromEnum(Enum<?> identity, int color, String name, Supplier<? extends Fluid> fluid)
    {
        final KFluidId type = new KFluidId(name, OptionalInt.of(TFCFluids.ALPHA_MASK | color), fluid);
        IDENTITY.put(identity, type);
        return type;
    }
}
