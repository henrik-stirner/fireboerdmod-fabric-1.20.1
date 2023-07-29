package net.henrik.fireboerdmod.client.render.entity.custom.model.layer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.client.render.entity.custom.model.ErrantFireModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {
    private static final String MAIN = "main";

    public static final EntityModelLayer ERRANT_FIRE = new EntityModelLayer(
            new Identifier(FireboerdMod.MOD_ID, "errant_fire"), MAIN);

    /**
     * Register entity model layers
     */

    public static void registerModEntityModelLayers() {
        FireboerdMod.LOGGER.info("Registering mod entity model layers for " + FireboerdMod.MOD_ID);

        EntityModelLayerRegistry.registerModelLayer(ERRANT_FIRE, ErrantFireModel::getTexturedModelData);
    }
}
