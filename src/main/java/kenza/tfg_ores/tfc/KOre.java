/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores.tfc;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraft.world.level.material.MapColor;

import net.dries007.tfc.util.registry.RegistryRock;

/**
 * Default ores used for block registration calls
 */
public enum KOre
{
    GALENA(true),
    STIBNITE(true),
    ZEOLITE(true),
    COBALTITE(true),

    VANADIUM_MAGNETITE(true),
    PENTLANDITE(true),
    GOETHITE(true),
    TANTALITE(true),
    CALCITE(true),
    LEPIDOLITE(true),
    BASTNASITE(true),
    PYROLUSITE(true),
    BARITE(true),
    BENTONITE(true),
    TRICALCIUM_PHOSPHATE(true),
    ASBESTOS(true),
    PYROCHLORE(true),
    MICA(true),
    KYANITE(true),
    ALUNITE(true),
    ELECTROTINE(true),
    DIATOMITE(true),
    NEODYMIUM(true),
    SOAPSTONE(true),
    TALC(true),
    GLAUCONITE(true),

    LITHIUM(true),
    BORNITE(true),
    PLATINUM(true),
    WULFENITE(true),
    MOLYBDENUM(true),
    PLUTONIUM(true),
    TUNGSTATE(true),
    SCHEELITE(true),
    PALLADIUM(true),
    POWELLITE(true),
    NAQUADAH(true),
    URANINITE(true),
    PITCHBLENDE(true);


    private final boolean graded;

    KOre(boolean graded)
    {
        this.graded = graded;
    }

    public boolean isGraded()
    {
        return graded;
    }

    public Block create(RegistryRock rock)
    {
        final BlockBehaviour.Properties properties = Block.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(rock.category().hardness(6.5f), 10).requiresCorrectToolForDrops();
        return new Block(properties);
    }

    public enum Grade
    {
        POOR, NORMAL, RICH;

        private static final Grade[] VALUES = values();

        public static Grade valueOf(int i)
        {
            return i < 0 || i >= VALUES.length ? NORMAL : VALUES[i];
        }
    }
}
