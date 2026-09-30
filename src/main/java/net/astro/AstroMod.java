package net.astro;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AstroMod implements ModInitializer {
    public static final String MOD_ID = "astro";

    public static final Block SECRET_COPPER_BLOCK = new SecretCopperBlock();

    @Override
    public void onInitialize() {
        Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "secret_copper_block"), SECRET_COPPER_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "secret_copper_block"), 
                new BlockItem(SECRET_COPPER_BLOCK, new Item.Settings()));

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient() && player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL)) {
                BlockPos pos = hitResult.getBlockPos();
                if (world.getBlockState(pos).isOf(Blocks.PURPUR_BLOCK)) {
                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        BlockPos dungeonCenter = new BlockPos(1000, 100, 1000);
                        generateDungeon(world, dungeonCenter);
                        
                        serverPlayer.requestTeleport(dungeonCenter.getX() + 0.5, dungeonCenter.getY() + 1.0, dungeonCenter.getZ() + 0.5);
                        
                        world.playSound(null, dungeonCenter, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        return ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }

    private void generateDungeon(net.minecraft.world.World world, BlockPos center) {
        int radius = 4;
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= 5; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos p = center.add(x, y, z);
                    if (x == -radius || x == radius || z == -radius || z == radius || y == 0 || y == 5) {
                        world.setBlockState(p, SECRET_COPPER_BLOCK.getDefaultState());
                    } else {
                        world.setBlockState(p, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }
}
