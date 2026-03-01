package net.snackbag.mcvera.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.snackbag.mcvera.impl.MCVeraRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if (>=1.21.11) {
/*import net.minecraft.client.gui.render.state.GuiRenderState;
import org.joml.Matrix3x2fStack;
*///?}

@Mixin(DrawContext.class)
@Environment(EnvType.CLIENT)
public abstract class DrawContextMixin {
    //? if (>=1.21.11) {
    /*@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lorg/joml/Matrix3x2fStack;Lnet/minecraft/client/gui/render/state/GuiRenderState;II)V")
    private void mcvera$updateVeraDrawContext(MinecraftClient client, Matrix3x2fStack matrices, GuiRenderState state, int mouseX, int mouseY, CallbackInfo ci) {
    *///?} else if (>=1.20.1) {
    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V")
    private void mcvera$updateVeraDrawContext(MinecraftClient client, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, CallbackInfo ci) {
    //?}
        MCVeraRenderer.drawContext = (DrawContext) ((Object) this);
    }
}
