package net.henrik.fireboerdmod.item.custom;

import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.henrik.fireboerdmod.visual_effect.VisualEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.function.Function;

public class VisualEffectItem extends Item {
    private final VisualEffect visualEffect;

    public VisualEffectItem(Settings settings, Object visualEffect) {
        super(settings);

        this.visualEffect = (VisualEffect) visualEffect;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }

        visualEffect.create(world, user.getPos());

        return TypedActionResult.success(user.getStackInHand(hand), true);
    }
}
