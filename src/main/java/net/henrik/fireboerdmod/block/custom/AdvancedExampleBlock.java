package net.henrik.fireboerdmod.block.custom;

import net.henrik.fireboerdmod.visual_effect.CylindricalFireScannerEffect;
import net.henrik.fireboerdmod.visual_effect.HomingFireEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AdvancedExampleBlock extends Block {
    public AdvancedExampleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        player.sendMessage(Text.literal("interacted with example block"), false);

        return ActionResult.SUCCESS;
    }
}
