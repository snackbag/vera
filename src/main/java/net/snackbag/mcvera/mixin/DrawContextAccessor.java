package net.snackbag.mcvera.mixin;

import net.minecraft.client.gui.DrawContext;
//? if (>=1.21.11) {
/*import net.minecraft.client.gui.render.state.GuiRenderState;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {
    //? if (<1.21.11) {
    @Invoker("tryDraw")
    void mcvera$invokeTryDraw();
    //?} else if (>=1.21.11) {
    /*@Accessor("state")
    GuiRenderState vera$getState();
    *///?}
}
