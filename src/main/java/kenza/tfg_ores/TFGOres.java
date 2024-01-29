package kenza.tfg_ores;

import kenza.tfg_ores.tfc.KTFCBlocks;
import kenza.tfg_ores.tfc.KTFCCreativeTabs;
import kenza.tfg_ores.tfc.KTFCItems;
import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(TFGOres.MOD_ID)
public class TFGOres
{
    public static final String MOD_ID = "tfg_ores";


    public void setup(FMLCommonSetupEvent event) {
        //PropickItem.registerDefaultRepresentativeBlocks();
    }


    public TFGOres() throws Throwable {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(EventPriority.LOWEST, KTFCCreativeTabs::onBuildCreativeTab); // Lowest priority, since we only modify existing items, not add new ones.

        KTFCBlocks.BLOCKS.register(bus);
        KTFCItems.ITEMS.register(bus);
//        KTFCFluids.FLUIDS.register(bus);
//        KTFCFluids.FLUID_TYPES.register(bus);
        KTFCCreativeTabs.CREATIVE_TABS.register(bus);


        bus.addListener(this::setup);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init();
        }

    }

}
