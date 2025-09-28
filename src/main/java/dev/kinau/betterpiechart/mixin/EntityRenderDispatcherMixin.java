package dev.kinau.betterpiechart.mixin;

import dev.kinau.betterpiechart.tracker.EntityTracker;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Inject(method = "extractEntity", at = @At("HEAD"))
    public <E extends Entity> void tryExtractEntityStart(E entity, float f, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityTracker tracker = EntityTracker.getInstance();
        tracker.add(entity);
        ProfilerFiller profiler = Profiler.get();
        tracker.getTag(entity).ifPresent(profiler::push);
        profiler.push(tracker.getName(entity));
    }

    @Inject(method = "extractEntity", at = @At(value = "RETURN"))
    private <E extends Entity> void tryExtractEntityEnd(E entity, float f, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityTracker tracker = EntityTracker.getInstance();
        ProfilerFiller profiler = Profiler.get();
        tracker.getTag(entity).ifPresent(s -> profiler.pop());
        profiler.pop();
    }

//	@Shadow @Final private List<Entity> visibleEntities;
//
//	private Map<String, Long> lastVisibleEntities;
//
//	@Inject(at = @At("HEAD"), method = "renderEntity")
//	private void renderEntityStart(Entity entity, double d, double e, double f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
//		Profiler.get().push(entity.getType().toShortString());
//		Profiler.get().incrementCounter(entity.getType().toShortString());
//	}
//
//	@Inject(at = @At(value = "TAIL"), method = "renderEntity")
//	private void renderEntityEnd(Entity entity, double d, double e, double f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info) {
//		Profiler.get().pop();
//	}
//
//	@Inject(method = "collectVisibleEntities", at = @At("RETURN"))
//	private void collectVisibleEntities(Camera camera, Frustum frustum, List<Entity> list, CallbackInfoReturnable<Boolean> cir) {
//		BlockEntityTracker.getInstance().clear();
//		this.lastVisibleEntities = visibleEntities.stream().collect(Collectors.groupingBy(entity -> entity.getType().toShortString(), Collectors.counting()))
//				.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
//	}
}