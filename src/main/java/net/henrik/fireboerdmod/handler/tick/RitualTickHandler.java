package net.henrik.fireboerdmod.handler.tick;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.ritual.FireboerdRitual;
import net.henrik.fireboerdmod.visual_effect.CylindricalFireScannerEffect;
import net.henrik.fireboerdmod.visual_effect.HomingFireEffect;
import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RitualTickHandler {
    private final RitualStartTickHandler START_TICK_HANDLER = new RitualStartTickHandler();
    private final RitualEndTickHandler END_TICK_HANDLER = new RitualEndTickHandler();

    public static List<FireboerdRitual> fireboerdRituals = new LinkedList<>();

    public static class RitualStartTickHandler implements ServerTickEvents.StartTick {
        @Override
        public void onStartTick(MinecraftServer server) {
            // by creating a copy of the list, the global one can be modified by the VisualEffect objects,
            // all while the for-loop is running
            List<FireboerdRitual> _fireboerdRituals = new ArrayList<>(fireboerdRituals);
            for (FireboerdRitual fireboerdRitual : _fireboerdRituals) {
                fireboerdRitual.onStartTick();
            }
        }


    }

    public static class RitualEndTickHandler implements ServerTickEvents.EndTick {
        @Override
        public void onEndTick(MinecraftServer server) {
            List<FireboerdRitual> _fireboerdRituals = new ArrayList<>(fireboerdRituals);
            for (FireboerdRitual fireboerdRitual : _fireboerdRituals) {
                fireboerdRitual.onEndTick();
            }
        }
    }

    public void registerTickHandlers() {
        FireboerdMod.LOGGER.info("Registering ritual tick handlers for " + FireboerdMod.MOD_ID);

        ServerTickEvents.START_SERVER_TICK.register(this.START_TICK_HANDLER);
        // NEVER USED:
        // ServerTickEvents.END_SERVER_TICK.register(this.END_TICK_HANDLER);
    }
}
