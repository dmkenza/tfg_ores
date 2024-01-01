package kenza.tfg_ores.debug;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static net.dries007.tfc.common.items.TFCItems.ITEMS;


public class DebugStick extends Item {


    public DebugStick(Properties p_41383_) {
        super(p_41383_);
    }


    public  ArrayList<String> list = new ArrayList();



    @Override
    public InteractionResult useOn(UseOnContext context) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Void>> futures = new ArrayList<>();



        if (context.getLevel().isClientSide) {
            BlockPos positionClicked = context.getClickedPos();
            Player player = context.getPlayer();

            int range = 100;

            for (int y = -range; y <= range; y++) {
                for (int x = -range; x <= range; x++) {
                    for (int z = -range; z <= range; z++) {

                        BlockState blockState = context.getLevel().getBlockState(
                            positionClicked.offset(x, y, z)
                        );

                        Block block = blockState.getBlock();

                        int x1 = positionClicked.offset(x, y, z).getX();
                        int y1 = positionClicked.offset(x, y, z).getY();
                        int z1 = positionClicked.offset(x, y, z).getZ();

                        if (isValuableBlock(block)) {
                            player.sendSystemMessage(Component.literal("Qwerty " + x1 + " " + y1 + " " + z1));
                            break;
                        }

                    }
                }
            }
        }
        return super.useOn(context);
    }


    private boolean isValuableBlock(Block block) {
        return block.toString().contains("poor_galena");
    }


    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return super.useOnRelease(pStack);
    }

    //    @Override
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        if(context.getWorld().isClient()) {
//            BlockPos positionClicked = context.getBlockPos();
//            PlayerEntity player = context.getPlayer();
//            boolean foundBlock = false;
//
//            for(int i = 0; i <= positionClicked.getY() + 64; i++) {
//                Block blockBelow = context.getWorld().getBlockState(positionClicked.down(i)).getBlock();
//
//                if(isValuableBlock(blockBelow)) {
//                    outputValuableCoordinates(positionClicked.down(i), player, blockBelow);
//                    foundBlock = true;
//                    break;
//                }
//            }
//
//            if(!foundBlock) {
//                player.sendMessage(new TranslatableText("item.tutorialmod.dowsing_rod.no_valuables"), false);
//            }
//        }
//
//        context.getStack().damage(1, context.getPlayer(),
//            (player) -> player.sendToolBreakStatus(player.getActiveHand()));
//
//        return super.useOnBlock(context);
//    }
//
//
//    private void outputValuableCoordinates(BlockPos blockPos, PlayerEntity player, Block blockBelow) {
//        player.sendMessage(new LiteralText("Found " + blockBelow.asItem().getName().getString() + " at " +
//            "(" + blockPos.getX() + ", " + blockPos.getY() + "," + blockPos.getZ() + ")"), false);
//    }
//
//    private boolean isValuableBlock(Block block) {
//        return block == Blocks.COAL_ORE || block == Blocks.COPPER_ORE
//            || block == Blocks.DIAMOND_ORE || block == Blocks.IRON_ORE;
//    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
