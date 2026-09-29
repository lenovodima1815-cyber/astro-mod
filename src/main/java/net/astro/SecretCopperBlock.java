package net.astro;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SecretCopperBlock extends Block {
    private static final Map<BlockPos, Integer> CLICK_COUNTS = new HashMap<>();

    public SecretCopperBlock() {
        super(Settings.copy(Blocks.COPPER_BLOCK));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            int clicks = CLICK_COUNTS.getOrDefault(pos, 0) + 1;
            
            if (clicks >= 5) {
                CLICK_COUNTS.put(pos, 0);
                ItemEntity diamond = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, new ItemStack(Items.DIAMOND));
                world.spawnEntity(diamond);
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0f, 1.0f);
            } else {
                CLICK_COUNTS.put(pos, clicks);
                world.playSound(null, pos, SoundEvents.BLOCK_COPPER_HIT, SoundCategory.BLOCKS, 1.0f, 0.8f + (clicks * 0.1f));
            }
        }
        return ActionResult.SUCCESS;
    }
}
