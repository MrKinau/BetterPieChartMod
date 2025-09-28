package dev.kinau.betterpiechart.mixin;

import dev.kinau.betterpiechart.tracker.BlockEntityTracker;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {

    @Inject(method = "tryExtractRenderState", at = @At("HEAD"))
    public <E extends BlockEntity> void tryExtractRenderStateStart(E blockEntity, float f, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfoReturnable<?> cir) {
        BlockEntityTracker tracker = BlockEntityTracker.getInstance();
        tracker.add(blockEntity);
        ProfilerFiller profiler = Profiler.get();
        tracker.getTag(blockEntity).ifPresent(profiler::push);
        profiler.push(tracker.getName(blockEntity));
    }

    @Inject(method = "tryExtractRenderState", at = @At(value = "RETURN"))
    private <E extends BlockEntity> void tryExtractRenderStateEnd(E blockEntity, float f, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfoReturnable<?> cir) {
        BlockEntityTracker tracker = BlockEntityTracker.getInstance();
        ProfilerFiller profiler = Profiler.get();
        tracker.getTag(blockEntity).ifPresent(s -> profiler.pop());
        profiler.pop();
    }
}
