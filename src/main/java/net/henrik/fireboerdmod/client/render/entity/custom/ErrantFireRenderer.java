package net.henrik.fireboerdmod.client.render.entity.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.client.render.entity.custom.model.ErrantFireModel;
import net.henrik.fireboerdmod.client.render.entity.custom.model.layer.ModEntityModelLayers;
import net.henrik.fireboerdmod.entity.projectile.ErrantFireEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class ErrantFireRenderer extends EntityRenderer<ErrantFireEntity> {
    private static final Identifier TEXTURE = new Identifier(FireboerdMod.MOD_ID, "textures/entity/errant_fire.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE);
    private final ErrantFireModel<ErrantFireEntity> model;

    public ErrantFireRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new ErrantFireModel<>(context.getPart(ModEntityModelLayers.ERRANT_FIRE));
    }

    @Override
    protected int getBlockLight(ErrantFireEntity errantFireEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(ErrantFireEntity errantFireEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = MathHelper.lerpAngleDegrees(g, errantFireEntity.prevYaw, errantFireEntity.getYaw());
        float j = MathHelper.lerp(g, errantFireEntity.prevPitch, errantFireEntity.getPitch());
        float k = (float)errantFireEntity.age + g;
        matrixStack.translate(0.0f, 0.15f, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(k * 0.1f) * 180.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.cos(k * 0.1f) * 180.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(k * 0.15f) * 360.0f));
        matrixStack.scale(-0.5f, -0.5f, 0.5f);
        this.model.setAngles(errantFireEntity, 0.0f, 0.0f, 0.0f, h, j);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.scale(1.5f, 1.5f, 1.5f);
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(LAYER);
        this.model.render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 0.15f);
        matrixStack.pop();
        super.render(errantFireEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(ErrantFireEntity errantFireEntity) {
        return TEXTURE;
    }
}
