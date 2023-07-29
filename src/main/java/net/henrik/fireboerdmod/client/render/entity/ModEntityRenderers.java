package net.henrik.fireboerdmod.client.render.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.client.render.entity.custom.ErrantFireRenderer;
import net.henrik.fireboerdmod.client.render.entity.custom.FireboerdRenderer;
import net.henrik.fireboerdmod.entity.ModEntityTypes;

public class ModEntityRenderers {

    /**
     * Register entity renderers
     */

    public static void registerModRenderers() {
        FireboerdMod.LOGGER.info("Registering mod entity renderers for " + FireboerdMod.MOD_ID);

        EntityRendererRegistry.register(ModEntityTypes.ERRANT_FIRE, ErrantFireRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.FIREBOERD, FireboerdRenderer::new);
    }
}
