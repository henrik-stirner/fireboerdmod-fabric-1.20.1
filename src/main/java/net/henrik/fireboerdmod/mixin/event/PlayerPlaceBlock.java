package net.henrik.fireboerdmod.mixin.event;

import net.henrik.fireboerdmod.handler.player.AltarCompletionHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 * <a href="https://github.com/Devan-Kerman/OneEvents/blob/master/LOOK%20IN%20HERE/PlayerPlaceBlock.java">
 *      professionally stolen from Devan Kerman's OneEvents (and obviously modified)
 * </a>
 */

/**
 * In order to implement an event handler, extend this class and override restrict() or onBlockPlaced()
 */

@Mixin(BlockItem.class)
public class PlayerPlaceBlock {
    @Unique
    private boolean restricted = false;

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private void onPlaceBlock(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(this.restrict(context, state)) {
            PlayerEntity entity = context.getPlayer();
            if(entity instanceof ServerPlayerEntity) {
                entity.getInventory().addPickBlock(entity.getStackInHand(context.getHand()));
            }

            cir.setReturnValue(false);
            this.restricted = true;
        } else {
            if (context.canPlace()) {
                if (context.getWorld().isClient()) {
                    return;
                }

                this.beforeBlockPlaced(context, state);
            }

            this.restricted = false;
        }
    }

    /**
     * @param context the context of the placement
     * @param state the state to place
     * @return return true if the player cannot place a block there
     */
    @Unique
    public boolean restrict(ItemPlacementContext context, BlockState state) {
        return false;
    }

    /**
     * @param context the context of the placement
     * @param state the state to place
     */
    @Unique
    public void beforeBlockPlaced(ItemPlacementContext context, BlockState state) {
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("TAIL"), cancellable = true)
    private void onBlockPlaced(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (this.restricted) {
            cir.setReturnValue(false);
        } else {
            if (context.getWorld().isClient()) {
                return;
            }

            this.afterBlockPlaced(context, state);
        }
    }

    /**
     * @param context the context of the placement
     * @param state the state to place
     */
    @Unique
    public void afterBlockPlaced(ItemPlacementContext context, BlockState state) {
        AltarCompletionHandler.checkForAltarCompletions(context, state);
    }
}
