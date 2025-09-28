package dev.kinau.betterpiechart.tracker;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class BlockEntityTracker extends Tracker<BlockEntity> {

    private static final BlockEntityTracker INSTANCE = new BlockEntityTracker();

    public static BlockEntityTracker getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName(BlockEntity blockEntity) {
        return BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock()).getPath();
    }

    @Override
    public Optional<String> getTag(BlockEntity blockEntity) {
        if (blockEntity.getBlockState().is(BlockTags.ALL_SIGNS))
            return Optional.of(BlockTags.ALL_SIGNS.location().getPath());
        if (blockEntity.getBlockState().is(BlockTags.BANNERS))
            return Optional.of(BlockTags.BANNERS.location().getPath());
        if (blockEntity.getBlockState().is(BlockTags.SHULKER_BOXES))
            return Optional.of(BlockTags.SHULKER_BOXES.location().getPath());
        if (blockEntity.getBlockState().is(BlockTags.BEDS))
            return Optional.of(BlockTags.BEDS.location().getPath());
        return Optional.empty();
    }
}
