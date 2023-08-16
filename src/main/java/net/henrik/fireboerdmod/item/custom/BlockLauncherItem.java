package net.henrik.fireboerdmod.item.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockLauncherItem extends Item {
    public BlockLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        BlockPos clickedBlockPos = context.getBlockPos();

        Random random = new Random();

        int range = 3;
        double speed = 1;

        for (BlockPos blockPos : BlockPos.iterateOutwards(clickedBlockPos, range, range, range)) {
            // would be a cube without this if-statement
            if (!blockPos.toCenterPos().isInRange(clickedBlockPos.toCenterPos(), range)) {
                // ignore blocks that are not in the sphere of the wanted radius
                continue;
            }

            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(
                    context.getWorld(),
                    blockPos,
                    context.getWorld().getBlockState(blockPos)
            );

            Vec3d moveToPos = context.getBlockPos().toCenterPos();
            Vec3d direction = moveToPos.subtract(blockPos.toCenterPos());
            double magnitude = direction.length();
            Vec3d velocity = direction.multiply(speed / magnitude);

            Vec3d noise = new Vec3d(
                    (random.nextDouble() - 0.5) * 2 * 0.125,
                    (random.nextDouble() - 0.5) * 2 * 0.125,
                    (random.nextDouble() - 0.5) * 2 * 0.125
            );

            fallingBlockEntity.setVelocity(velocity.add(noise));
        }

        return ActionResult.SUCCESS;
    }
}
