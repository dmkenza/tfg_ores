package kenza.tfg_ores.mixin;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.tags.BlockTags;

@Mixin(NoteBlockInstrument.class)
@Unique
public abstract class NoteblockInstrumentMixin {

    @Shadow
    @Final
    @Mutable
    public static NoteBlockInstrument[] $VALUES;

    //private static final NoteBlockInstrument MUSIC_BOX = noteblockExpansion$addVariant("MUSIC_BOX", "music_box", SoundEvents.NOTE_BLOCK_HARP.get());
//    private static final NoteBlockInstrument PIANO = noteblockExpansion$addVariant("PIANO", "piano", NoteblockSounds.PIANO);
//    private static final NoteBlockInstrument SITAR = noteblockExpansion$addVariant("SITAR", "sitar", NoteblockSounds.SITAR);
//    private static final NoteBlockInstrument VIBRAPHONE = noteblockExpansion$addVariant("VIBRAPHONE", "vibraphone", NoteblockSounds.VIBRAPHONE);
//    private static final NoteBlockInstrument BASSDRUM = noteblockExpansion$addVariant("BASSDRUM", "bassdrum", NoteblockSounds.BASSDRUM);
//    private static final NoteBlockInstrument CELESTA = noteblockExpansion$addVariant("CELESTA", "celesta", NoteblockSounds.CELESTA);
//    private static final NoteBlockInstrument GLOCKENSPIEL = noteblockExpansion$addVariant("GLOCKENSPIEL", "glockenspiel", NoteblockSounds.GLOCKENSPIEL);
//    private static final NoteBlockInstrument HARPSICHORD = noteblockExpansion$addVariant("HARPSICHORD", "harpsichord", NoteblockSounds.HARPSICHORD);
//    private static final NoteBlockInstrument HIGHDRUM = noteblockExpansion$addVariant("HIGHDRUM", "highdrum", NoteblockSounds.HIGHDRUM);
//    private static final NoteBlockInstrument MARIMBA = noteblockExpansion$addVariant("MARIMBA", "marimba", NoteblockSounds.MARIMBA);
//    private static final NoteBlockInstrument STEELDRUMS = noteblockExpansion$addVariant("STEELDRUMS", "steeldrums", NoteblockSounds.STEELDRUMS);
//    private static final NoteBlockInstrument TIMPANI = noteblockExpansion$addVariant("TIMPANI", "timpani", NoteblockSounds.TIMPANI);
//    private static final NoteBlockInstrument WOODBLOCKS = noteblockExpansion$addVariant("WOODBLOCKS", "woodblocks", NoteblockSounds.WOODBLOCKS);
//    private static final NoteBlockInstrument PLUCK = noteblockExpansion$addVariant("PLUCK", "pluck", NoteblockSounds.PLUCK);
//    private static final NoteBlockInstrument CHINESE_PIK = noteblockExpansion$addVariant("CHINESE_PIK", "chinese_pik", NoteblockSounds.CHINESE_PIK);

//    @Invoker("<init>")
//    public static NoteBlockInstrument noteblockExpansion$invokeInit(String internalName, int internalId, String name, SoundEvent sound) {
//        throw new AssertionError();
//    }

//    private static NoteBlockInstrument noteblockExpansion$addVariant(String internalName, String name, SoundEvent sound) {
//        ArrayList<NoteBlockInstrument> variants = new ArrayList<NoteBlockInstrument>(Arrays.asList(NoteblockInstrumentMixin.$VALUES));
//        NoteBlockInstrument instrument = noteblockExpansion$invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, name, sound);
//        variants.add(instrument);
//        NoteblockInstrumentMixin.$VALUES = variants.toArray(new NoteBlockInstrument[0]);
//        return instrument;
//    }

//    @Inject(method = "byState", at = @At("HEAD"), cancellable = true)
//    private static void noteblockExpansion$byState(BlockState state, CallbackInfoReturnable<NoteBlockInstrument> ci) {
//        if (state.isIn(Blocks.SANDSTONE) || state.isIn(Blocks.RED_SANDSTONE)) {
//            ci.setReturnValue(SITAR);
//        } else if (state.isIn(BlockTags.PLANKS)) {
//            ci.setReturnValue(PIANO);
//        } else if (state.isIn(Blocks.DIAMOND_BLOCK)) {
//            ci.setReturnValue(MUSIC_BOX);
//        } else if (state.isIn(Blocks.BLUE_ICE)) {
//            ci.setReturnValue(VIBRAPHONE);
//        } else if (state.isIn(Blocks.BARREL)) {
//            ci.setReturnValue(TIMPANI);
//        } else if (state.isIn(Blocks.LOOM)) {
//            ci.setReturnValue(HARPSICHORD);
//        } else if (state.isIn(BlockTags.LOGS)) {
//            ci.setReturnValue(MARIMBA);
//        } else if (state.isIn(Blocks.IRON_ORE)) {
//            ci.setReturnValue(GLOCKENSPIEL);
//        } else if (state.isIn(Blocks.GOLD_ORE)) {
//            ci.setReturnValue(CELESTA);
//        } else if (state.isIn(Blocks.DIRT)) {
//            ci.setReturnValue(STEELDRUMS);
//        } else if (state.isIn(BlockTags.WOODEN_TRAPDOORS)) {
//            ci.setReturnValue(WOODBLOCKS);
//        } else if (state.isIn(Blocks.CHEST)) {
//            ci.setReturnValue(BASSDRUM);
//        } else if (state.isIn(Blocks.TRAPPED_CHEST)) {
//            ci.setReturnValue(HIGHDRUM);
//        } else if (state.isIn(Blocks.END_STONE)) {
//            ci.setReturnValue(CHINESE_PIK);
//        } else if (state.isIn(Blocks.OBSIDIAN)) {
//            ci.setReturnValue(PLUCK);
//        }
//    }
}