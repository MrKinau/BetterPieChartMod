package dev.kinau.betterpiechart.mixin;

import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRenderDispatcherMixin {

    @Inject(method = "renderAllFeatures", at = @At("HEAD"))
    private void renderAllFeaturesStart(CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("renderAllFeatures");
    }

    @Inject(method = "renderAllFeatures", at = @At("RETURN"))
    private void renderAllFeaturesEnd(CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        profiler.pop();
    }
}
