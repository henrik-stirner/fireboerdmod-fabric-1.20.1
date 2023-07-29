package net.henrik.fireboerdmod.client.render.entity.custom.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;

@Environment(value= EnvType.CLIENT)
public class ErrantFireModel<T extends Entity> extends SinglePartEntityModel<T> {
    /**
     * The key of the main model part, whose value is {@value}.
     */
    private static final String MAIN = "main";
    private final ModelPart root;
    private final ModelPart bullet;

    public ErrantFireModel(ModelPart root) {
        this.root = root;
        this.bullet = root.getChild(MAIN);
    }

    // export a model in BlockBench to get this method
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData main = modelPartData.addChild(MAIN, ModelPartBuilder.create().uv(0, 12).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(18, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 16).cuboid(-2.0F, 3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 12).cuboid(3.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 20).cuboid(-4.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(20, 21).cuboid(-2.0F, -2.0F, 3.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 21).cuboid(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.bullet.yaw = headYaw * ((float)Math.PI / 180);
        this.bullet.pitch = headPitch * ((float)Math.PI / 180);
    }
}