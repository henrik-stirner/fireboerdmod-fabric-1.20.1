package net.henrik.fireboerdmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.item.custom.AdvancedExampleItem;
import net.henrik.fireboerdmod.item.custom.ErrantFireItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // public static final Item EXAMPLE_ITEM = registerItem("example_item", new Item(new FabricItemSettings()));
    public static final Item EXAMPLE_ITEM = registerItem("example_item", new AdvancedExampleItem(
            new FabricItemSettings()));

    // public static final Item EXAMPLE_FOOD = registerItem("example_food", new AdvancedExampleItem(
    //        new FabricItemSettings().food(ModFoodComponents.EXAMPLE_FOOD)));

    public static final Item ERRANT_FIRE = registerItem("errant_fire", new ErrantFireItem(
            new FabricItemSettings()));

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
