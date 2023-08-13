package net.henrik.fireboerdmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.henrik.fireboerdmod.block.ModBlocks;
import net.henrik.fireboerdmod.item.ModItems;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.X);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ERRANT_FIRE, Models.GENERATED);

        itemModelGenerator.register(ModItems.SMOKE_BALL, Models.GENERATED);
        itemModelGenerator.register(ModItems.FIRE_SCANNER, Models.GENERATED);
        itemModelGenerator.register(ModItems.HOMING_FIRE, Models.GENERATED);
    }
}
