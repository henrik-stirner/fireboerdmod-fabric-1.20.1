package net.henrik.fireboerdmod;

import net.fabricmc.api.ClientModInitializer;
import net.henrik.fireboerdmod.client.render.entity.ModEntityRenderers;
import net.henrik.fireboerdmod.client.render.entity.custom.model.layer.ModEntityModelLayers;

public class FireboerdModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Everything entity-rendering
        ModEntityRenderers.registerModRenderers();
        ModEntityModelLayers.registerModEntityModelLayers();
    }
}
