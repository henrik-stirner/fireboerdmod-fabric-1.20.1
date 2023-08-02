package net.henrik.fireboerdmod.item.custom;

import net.henrik.fireboerdmod.entity.projectile.ErrantFireEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class EntitySpawningItem extends Item {
    private final EntityType<? extends Entity> entityType;

    public EntitySpawningItem(Settings settings, EntityType<? extends Entity> entityType) {
        super(settings);

        this.entityType = entityType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(user.getStackInHand(hand), true);
        }

        entityType.spawnFromItemStack(
                (ServerWorld) world,
                user.getStackInHand(hand),
                user,
                user.getBlockPos(),
                SpawnReason.MOB_SUMMONED,
                true,
                false
        );

        return TypedActionResult.success(user.getStackInHand(hand), true);
    }
}
