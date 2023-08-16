package net.henrik.fireboerdmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.entity.ModEntityTypes;
import net.henrik.fireboerdmod.item.custom.*;
import net.henrik.fireboerdmod.visual_effect.CylindricalFireScannerEffect;
import net.henrik.fireboerdmod.visual_effect.HomingFireEffect;
import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
//    public static final Item EXAMPLE_ITEM = registerItem("example_item", new Item(new FabricItemSettings()));

//    public static final Item EXAMPLE_FOOD = registerItem("example_food", new Item(
//            new FabricItemSettings().food(ModFoodComponents.EXAMPLE_FOOD)));

    /* PROJECTILES */
    public static final Item ERRANT_FIRE = registerItem("errant_fire", new ErrantFireItem(
            new FabricItemSettings()));

    /* PROJECTILE EFFECTS */
    // ...

    /* PARTICLE EFFECTS */
    public static final Item SMOKE_BALL = registerItem("smoke_ball", new VisualEffectItem(
            new FabricItemSettings(),
            new SmokeBallEffect(null, null, 60, 3.0d)
    ));
    public static final Item FIRE_SCANNER = registerItem("fire_scanner", new VisualEffectItem(
            new FabricItemSettings(),
            new CylindricalFireScannerEffect(null, null, 200, 1.5d, 5)
    ));
    public static final Item HOMING_FIRE = registerItem("homing_fire", new VisualEffectItem(
            new FabricItemSettings(),
            new HomingFireEffect(null, null, 60)
    ));

    /* MISC */
    public static final Item BLOCK_LAUNCHER = registerItem("block_launcher", new BlockLauncherItem(
            new FabricItemSettings()
    ));

    /**
     * Add items to the corresponding tab in the creative menu
     */

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        // entries.add(EXAMPLE_ITEM);
        // ...
    }

    /**
     * Register items
     */

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FireboerdMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FireboerdMod.LOGGER.info("Registering mod items for " + FireboerdMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
