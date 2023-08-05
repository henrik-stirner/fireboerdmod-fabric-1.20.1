package net.henrik.fireboerdmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.block.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    /**
     * Add item groups to the creative menu
     */

    public static final ItemGroup FIERY_GROUP = registerItemGroup("fiery_group",
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.fireboerdmod.fiery_group"))
                    .icon(() -> new ItemStack(ModItems.ERRANT_FIRE)).entries((displayContext, entries) -> {
                        entries.add(ModItems.ERRANT_FIRE);

                        entries.add(ModItems.SMOKE_BALL);
                        entries.add(ModItems.FIRE_SCANNER);
                        entries.add(ModItems.HOMING_FIRE);
                        // ...
                    }).build());

    /**
     * Register item groups
     */

    private static ItemGroup registerItemGroup(String name, ItemGroup itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, new Identifier(FireboerdMod.MOD_ID, name), itemGroup);
    }

    public static void registerModItemGroups() {
        FireboerdMod.LOGGER.info("Registering mod item groups for " + FireboerdMod.MOD_ID);
    }
}
