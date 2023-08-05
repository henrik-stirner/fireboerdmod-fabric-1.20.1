package net.henrik.fireboerdmod.event;

import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.event.custom.tick.VisualEffectTickHandler;

public class ModEventHandlers {
    public static final VisualEffectTickHandler VISUAL_EFFECT_TICK_HANDLER = new VisualEffectTickHandler();

    public static void registerModEvents() {
        FireboerdMod.LOGGER.info("Registering mod events for " + FireboerdMod.MOD_ID);

        VISUAL_EFFECT_TICK_HANDLER.registerTickHandlers();
    }
}
