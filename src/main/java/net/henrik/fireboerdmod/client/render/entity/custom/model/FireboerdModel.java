package net.henrik.fireboerdmod.client.render.entity.custom.model;

import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FireboerdModel extends GeoModel<FireboerdEntity> {
    @Override
    public Identifier getModelResource(FireboerdEntity animatable) {
        return new Identifier(FireboerdMod.MOD_ID, "geo/fireboerd.geo.json");
    }

    @Override
    public Identifier getTextureResource(FireboerdEntity animatable) {
        return new Identifier(FireboerdMod.MOD_ID, "textures/entity/fireboerd.png");
    }

    @Override
    public Identifier getAnimationResource(FireboerdEntity animatable) {
        return new Identifier(FireboerdMod.MOD_ID, "animations/fireboerd.animation.json");
    }

    @Override
    public void setCustomAnimations(FireboerdEntity animatable, long instanceId, AnimationState<FireboerdEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
