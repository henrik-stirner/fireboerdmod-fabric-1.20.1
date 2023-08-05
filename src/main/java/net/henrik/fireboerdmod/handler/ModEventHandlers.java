package net.henrik.fireboerdmod.handler;

import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.handler.tick.RitualTickHandler;
import net.henrik.fireboerdmod.handler.tick.VisualEffectTickHandler;

public class ModEventHandlers {
    public static final VisualEffectTickHandler VISUAL_EFFECT_TICK_HANDLER = new VisualEffectTickHandler();
    public static final RitualTickHandler RITUAL_TICK_HANDLER = new RitualTickHandler();

    public static void registerModEvents() {
        FireboerdMod.LOGGER.info("Registering mod events for " + FireboerdMod.MOD_ID);

        VISUAL_EFFECT_TICK_HANDLER.registerTickHandlers();
        RITUAL_TICK_HANDLER.registerTickHandlers();
    }
}
