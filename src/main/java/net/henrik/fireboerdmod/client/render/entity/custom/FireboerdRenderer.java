package net.henrik.fireboerdmod.client.render.entity.custom;

import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.client.render.entity.custom.model.FireboerdModel;
import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FireboerdRenderer extends GeoEntityRenderer<FireboerdEntity> {
    public FireboerdRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FireboerdModel());
    }

    @Override
    public Identifier getTextureLocation(FireboerdEntity animatable) {
        return new Identifier(FireboerdMod.MOD_ID, "textures/entity/fireboerd.png");
    }

    @Override
    public void render(
            FireboerdEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,
            VertexConsumerProvider bufferSource, int packedLight
    ) {
        if (entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
