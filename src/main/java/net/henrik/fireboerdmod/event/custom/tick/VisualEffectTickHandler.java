package net.henrik.fireboerdmod.event.custom.tick;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.visual_effect.CylindricalFireScannerEffect;
import net.henrik.fireboerdmod.visual_effect.HomingFireEffect;
import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.minecraft.server.MinecraftServer;

public class VisualEffectTickHandler {
    private final VisualEffectStartTickHandler START_TICK_HANDLER = new VisualEffectStartTickHandler();
    private final VisualEffectEndTickHandler END_TICK_HANDLER = new VisualEffectEndTickHandler();

    public static List<CylindricalFireScannerEffect> cylindricalFireScannerEffects = new LinkedList<>();
    public static List<HomingFireEffect> homingFireEffects = new LinkedList<>();
    public static List<SmokeBallEffect> smokeBallEffects = new LinkedList<>();

    public static class VisualEffectStartTickHandler implements ServerTickEvents.StartTick {
        @Override
        public void onStartTick(MinecraftServer server) {
            // by creating a copy of the list, the global one can be modified by the VisualEffect objects,
            // all while the for-loop is running
            List<CylindricalFireScannerEffect> _cylindricalFireScannerEffects = new ArrayList<>(cylindricalFireScannerEffects);
            for (CylindricalFireScannerEffect cylindricalFireScannerEffect : _cylindricalFireScannerEffects) {
                cylindricalFireScannerEffect.onStartTick();
            }

            List<HomingFireEffect> _homingFireEffects = new ArrayList<>(homingFireEffects);
            for (HomingFireEffect homingFireEffect : _homingFireEffects) {
                homingFireEffect.onStartTick();
            }

            List<SmokeBallEffect> _smokeBallEffects = new ArrayList<>(smokeBallEffects);
            for (SmokeBallEffect smokeBallEffect : _smokeBallEffects) {
                smokeBallEffect.onStartTick();
            }
        }


    }

    public static class VisualEffectEndTickHandler implements ServerTickEvents.EndTick {
        @Override
        public void onEndTick(MinecraftServer server) {
            List<CylindricalFireScannerEffect> _cylindricalFireScannerEffects = new ArrayList<>(cylindricalFireScannerEffects);
            for (CylindricalFireScannerEffect cylindricalFireScannerEffect : _cylindricalFireScannerEffects) {
                cylindricalFireScannerEffect.onEndTick();
            }

            List<HomingFireEffect> _homingFireEffects = new ArrayList<>(homingFireEffects);
            for (HomingFireEffect homingFireEffect : _homingFireEffects) {
                homingFireEffect.onEndTick();
            }

            List<SmokeBallEffect> _smokeBallEffects = new ArrayList<>(smokeBallEffects);
            for (SmokeBallEffect smokeBallEffect : _smokeBallEffects) {
                smokeBallEffect.onEndTick();
            }
        }
    }

    public void registerTickHandlers() {
        ServerTickEvents.START_SERVER_TICK.register(this.START_TICK_HANDLER);
        // NEVER USED:
        // ServerTickEvents.END_SERVER_TICK.register(this.END_TICK_HANDLER);
    }
}
