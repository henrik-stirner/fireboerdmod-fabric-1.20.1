package net.henrik.fireboerdmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.projectile.ErrantFireEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityTypes {

    public static final EntityType<ErrantFireEntity> ERRANT_FIRE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(FireboerdMod.MOD_ID, "errant_fire"),
            FabricEntityTypeBuilder.<ErrantFireEntity>create(SpawnGroup.MISC, ErrantFireEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<FireboerdEntity> FIREBOERD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(FireboerdMod.MOD_ID, "fireboerd"),
            FabricEntityTypeBuilder.<FireboerdEntity>create(SpawnGroup.CREATURE, FireboerdEntity::new)
                    .dimensions(EntityDimensions.fixed(1.75f, 3.0f))
                    .build()
    );

    /**
     * Register entity types
     */

    public static void registerModEntityTypes() {
        FireboerdMod.LOGGER.info("Registering mod entity types for " + FireboerdMod.MOD_ID);
    }

    public static void registerModEntityTypeDefaultAttributes() {
        FireboerdMod.LOGGER.info("Registering mod entity type default attributes for " + FireboerdMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(ModEntityTypes.FIREBOERD, FireboerdEntity.setAttributes());
    }
}
