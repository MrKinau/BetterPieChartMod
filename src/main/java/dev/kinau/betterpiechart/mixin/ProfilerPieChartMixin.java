package dev.kinau.betterpiechart.mixin;

import dev.kinau.betterpiechart.tracker.BlockEntityTracker;
import dev.kinau.betterpiechart.tracker.EntityTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProfilerPieChart.class)
public abstract class ProfilerPieChartMixin {

    @Shadow private String profilerTreePath;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    private void renderPieChartText(GuiGraphics instance, Font font, String string, int i, int j, int k) {
        if (!string.startsWith("[")) {
            instance.drawString(font, string, i, j, k);
            return;
        }
        if (string.startsWith("[0] root.") || string.equals("[?] unspecified")) {
            instance.drawString(font, string, i, j, k);
            return;
        }
        int lastControlCharacter = profilerTreePath.lastIndexOf(30);
        String last = profilerTreePath.substring(Math.max(lastControlCharacter + 1, 0));
        String previousPath = profilerTreePath.substring(0, Math.max(lastControlCharacter, 0));
        int previousControlCharacter = previousPath.lastIndexOf(30);
        String previous = previousPath.substring(Math.max(previousControlCharacter + 1, 0));
        if (last.equals("entities") || previous.equals("entities")) {
            int firstBracketClose = string.indexOf(']');
            if (firstBracketClose < 0) {
                instance.drawString(font, string, i, j, k);
                return;
            }
            String entityOrTag = string.substring(firstBracketClose + 2);
            instance.drawString(font, string + " (" + EntityTracker.getInstance().counts().getOrDefault(entityOrTag, 0L) + ")", i, j, k);
            return;
        } else if (last.equals("blockEntities") || previous.equals("blockEntities")) {
            int firstBracketClose = string.indexOf(']');
            if (firstBracketClose < 0) {
                instance.drawString(font, string, i, j, k);
                return;
            }
            String blockEntityOrTag = string.substring(firstBracketClose + 2);
            instance.drawString(font, string + " (" + BlockEntityTracker.getInstance().counts().getOrDefault(blockEntityOrTag, 0L) + ")", i, j, k);
            return;
        }
        instance.drawString(font, string, i, j, k);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void renderPieChartEnd(GuiGraphics guiGraphics, CallbackInfo ci) {
        BlockEntityTracker.getInstance().clear();
        EntityTracker.getInstance().clear();
    }
}
