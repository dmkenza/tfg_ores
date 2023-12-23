/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores;

import net.dries007.tfc.client.*;
import kenza.tfg_ores.tfc.KTFCBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ClientEventHandler
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::clientSetup);
//        bus.addListener(ClientEventHandler::registerModelLoaders);
//        bus.addListener(ClientEventHandler::registerSpecialModels);
//        bus.addListener(ClientEventHandler::registerColorHandlerBlocks);
//        bus.addListener(ClientEventHandler::registerColorHandlerItems);
//        bus.addListener(ClientEventHandler::registerColorResolvers);
//        bus.addListener(ClientEventHandler::registerParticleFactories);
//        bus.addListener(ClientEventHandler::registerClientReloadListeners);
//        bus.addListener(ClientEventHandler::registerEntityRenderers);
//        bus.addListener(ClientEventHandler::registerKeyBindings);
//        bus.addListener(ClientEventHandler::onTooltipFactoryRegistry);
//        bus.addListener(ClientEventHandler::registerLayerDefinitions);
//        bus.addListener(ClientEventHandler::registerPresetEditors);
        bus.addListener(IngameOverlays::registerOverlays);
    }

    @SuppressWarnings("deprecation")
    public static void clientSetup(FMLClientSetupEvent event)
    {

        // Render Types
        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();

        KTFCBlocks.ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        KTFCBlocks.GRADED_ORES.values().forEach(map -> map.values().forEach(inner -> inner.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout))));
//        TFCBlocks.ORE_DEPOSITS.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));


        // Screens
        event.enqueueWork(() -> {


        });

    }

}