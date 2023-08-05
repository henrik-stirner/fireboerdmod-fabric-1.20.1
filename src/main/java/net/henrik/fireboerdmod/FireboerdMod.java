package net.henrik.fireboerdmod;

import net.fabricmc.api.ModInitializer;

import net.henrik.fireboerdmod.block.ModBlocks;
import net.henrik.fireboerdmod.entity.ModEntityTypes;
import net.henrik.fireboerdmod.handler.ModEventHandlers;
import net.henrik.fireboerdmod.item.ModItemGroups;
import net.henrik.fireboerdmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FireboerdMod implements ModInitializer {
	public static final String MOD_ID = "fireboerdmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_ID);

		ModItemGroups.registerModItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerModEntityTypeDefaultAttributes();

		ModEventHandlers.registerModEvents();
	}
}