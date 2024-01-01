/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package kenza.tfg_ores;

import kenza.tfg_ores.tfc.KMetal;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.client.*;
import kenza.tfg_ores.tfc.KTFCBlocks;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.common.blocks.wood.Wood.BlockType.LEAVES;

public final class ClientEventHandler
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::clientSetup);
//        bus.addListener(ClientEventHandler::registerModelLoaders);
//        bus.addListener(ClientEventHandler::registerSpecialModels);
//        bus.addListener(ClientEventHandler::registerColorHandlerBlocks);
        bus.addListener(ClientEventHandler::registerColorHandlerItems);
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

//    public static void registerColorHandlerBlocks(RegisterColorHandlersEvent.Block event)
//    {
//        Blocks.METAL_CAULDRONS.forEach((metal, reg) -> event.register((state, level, pos, tintIndex) -> metal.getColor(), reg.get()));
//    }

    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {
        for (Fluid fluid : ForgeRegistries.FLUIDS.getValues())
        {
            if (Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).getNamespace().equals(MOD_ID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void clientSetup(FMLClientSetupEvent event)
    {

        // Render Types
        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();

        KTFCBlocks.ORES.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));
        KTFCBlocks.GRADED_ORES.values().forEach(map -> map.values().forEach(inner -> inner.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout))));
        TFCBlocks.SMALL_ORES.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout));
        // Metal blocks
        KTFCBlocks.METALS.values().forEach(map -> map.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(reg.get(), cutout)));

        // Screens
        event.enqueueWork(() -> {


        });

    }

}