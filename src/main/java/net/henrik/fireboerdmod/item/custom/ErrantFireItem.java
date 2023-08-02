package net.henrik.fireboerdmod.item.custom;

import net.henrik.fireboerdmod.entity.projectile.ErrantFireEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ErrantFireItem extends Item {
    public ErrantFireItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }

        ErrantFireEntity errantFireEntity = new ErrantFireEntity(
                world,
                user,
                world.getClosestPlayer(user, 10),
                Direction.Axis.pickRandomAxis(Random.create())
        );
        world.spawnEntity(errantFireEntity);

        return TypedActionResult.success(user.getStackInHand(hand), true);
    }
}
