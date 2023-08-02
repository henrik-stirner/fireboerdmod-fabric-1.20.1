package net.henrik.fireboerdmod.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AdvancedExampleItem extends Item {
    public AdvancedExampleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }

        user.sendMessage(Text.literal("used example item"), false);

        return TypedActionResult.success(user.getStackInHand(hand), true);
    }
}
