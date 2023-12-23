package kenza.tfg_ores.mixin;

import net.dries007.tfc.common.TFCCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TFCCreativeTabs.class)
public class TFCCreativeTabsMixin {


//    @Inject(at = @At("HEAD"), method = "fillOresTab")
//    private static void tfg_ores$fillOresTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out, CallbackInfo ci){
//        TFGCreativeTabs.tfg_ores$fillOresTab(parameters, out);
//    }
}
