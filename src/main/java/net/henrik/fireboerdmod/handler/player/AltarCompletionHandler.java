package net.henrik.fireboerdmod.handler.player;

import net.henrik.fireboerdmod.ritual.FireboerdRitual;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.Vec3d;

public class AltarCompletionHandler {
    public static void checkForAltarCompletions(ItemPlacementContext context, BlockState state) {
        if (FireboerdRitual.checkPossibleAltarCompletion(context, state)) {
            new FireboerdRitual(
                    context.getWorld(),
                    context.getBlockPos()
            );
        }
    }
}
