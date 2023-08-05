package net.henrik.fireboerdmod.ritual;

import net.henrik.fireboerdmod.handler.tick.RitualTickHandler;
import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class FireboerdRitual extends Ritual {

    public final Integer duration = 200;

    protected static final List<Identifier> allowedDimensions = List.of(
            new Identifier("the_nether")
    );
    protected static final Item finishingBlockItem = Items.OBSIDIAN;

    protected static final Map<Vec3i, Block> layout = Map.of(
            new Vec3i(0, 0, 0), Blocks.OBSIDIAN,
            new Vec3i(0, -1, 0), Blocks.OBSIDIAN
    );

    public FireboerdRitual(World world, Vec3d position) {
        super(world, position);

        RitualTickHandler.fireboerdRituals.add(this);
    }

    public static boolean checkPossibleAltarCompletion(ItemPlacementContext context, BlockState state) {
        if (!(allowedDimensions.contains(context.getWorld().getRegistryKey().getValue()))) {
            return false;
        }

        if (!context.getStack().isOf(finishingBlockItem)) {
            return false;
        }

        for (Vec3i position : layout.keySet()) {
            Vec3i positionInWorld = position.add(context.getBlockPos());
            Block block = context.getWorld().getBlockState(new BlockPos(positionInWorld)).getBlock();

            if (!(block == layout.get(position))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onStartTick() {
        if (this.world.isClient()) {
            return;
        }

        if (this.ticks >= this.duration) {
            RitualTickHandler.fireboerdRituals.remove(this);
        }

        if (this.ticks == 0) {
            new SmokeBallEffect(this.world, this.position, this.duration, 3);
        }

        ++this.ticks;
    }
}
