package net.henrik.fireboerdmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.henrik.fireboerdmod.block.ModBlocks;
import net.henrik.fireboerdmod.entity.ModEntityTypes;
import net.henrik.fireboerdmod.event.VisualEffectTickHandler;
import net.henrik.fireboerdmod.item.ModItemGroups;
import net.henrik.fireboerdmod.item.ModItems;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.tick.TickScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FireboerdMod implements ModInitializer {
	public static final String MOD_ID = "fireboerdmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public final VisualEffectTickHandler VISUAL_EFFECT_TICK_HANDLER = new VisualEffectTickHandler();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_ID);

		ModItemGroups.registerModItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerModEntityTypeDefaultAttributes();

		VISUAL_EFFECT_TICK_HANDLER.registerTickHandlers();
	}
}