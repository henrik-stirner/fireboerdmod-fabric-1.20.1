package net.henrik.fireboerdmod.block;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.block.custom.AdvancedExampleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // public static final Block EXAMPLE_BLOCK = registerBlock("example_block",
    //        new Block(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block EXAMPLE_BLOCK = registerBlock("example_block",
            new AdvancedExampleBlock(FabricBlockSettings.copyOf(Blocks.STONE)));

    /**
     * Register blocks and block items
     */

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);

        return Registry.register(Registries.BLOCK, new Identifier(FireboerdMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(FireboerdMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        FireboerdMod.LOGGER.info("Registering mod blocks for " + FireboerdMod.MOD_ID);
    }
}
