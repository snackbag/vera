package net.snackbag.mcvera.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
//? if (>=1.21.11) {
/*import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.gui.render.state.TextGuiElementRenderState;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.util.Window;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.client.gl.RenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix3x2fStack;
*///?}
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.snackbag.mcvera.MCVeraData;
import net.snackbag.mcvera.mixin.DrawContextAccessor;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.flag.VAppFlag;
import net.snackbag.vera.flag.VAppPositioningFlag;
import net.snackbag.vera.widget.VWidget;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_BLEND;

public class MCVeraRenderer {
    public static DrawContext drawContext = null;

    //
    // Widget rendering
    //

    public void pushContext(VRenderContext ctx) {
        //? if (>=1.21.11) {
        /*Matrix3x2fStack stack = drawContext.getMatrices();
        stack.pushMatrix();
        *///?} else if (>=1.20.1) {
        MatrixStack stack = drawContext.getMatrices();
        stack.push();
        //?}

        float wMod = (ctx.width / 2f) * (ctx.scale - 1);
        float hMod = (ctx.height / 2f) * (ctx.scale - 1);

        float xRot = ctx.x + ctx.width / 2f;
        float yRot = ctx.y + ctx.height / 2f;

        // Rotation

        //? if (>=1.21.11) {
        /*stack.translate(xRot, yRot);
        stack.rotate((float) Math.toRadians(ctx.rotation));
        stack.translate(-xRot, -yRot);
        *///?} else if (>=1.20.1) {
        stack.translate(xRot, yRot, 0f);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(ctx.rotation));
        stack.translate(-xRot, -yRot, 0f);
        //?}

        // Scale & final positioning)
        //? if (>=1.21.11) {
        /*stack.translate(ctx.x - wMod, ctx.y - hMod);
        stack.scale(ctx.scale, ctx.scale);
        *///?} else if (>=1.20.1) {
        stack.translate(ctx.x - wMod, ctx.y - hMod, 0f);
        stack.scale(ctx.scale, ctx.scale, 1.0f);
        //?}
    }

    public void popContext() {
        //? if (>=1.21.11) {
        /*drawContext.getMatrices().popMatrix();
        *///?} else if (>=1.20.1) {
        drawContext.getMatrices().pop();
        //?}
    }

    public void drawRect(VRenderContext ctx, int x, int y, int width, int height, VColor color) {
        renderColQuad(
                x, y,
                x, y + height,
                x + width, y + height,
                x + width, y, color
        );
    }

    public void drawText(VRenderContext ctx, int x, int y, String text, VFont font) {
        //? if (>=1.21.11) {
        /*Matrix3x2fStack stack = drawContext.getMatrices();
        stack.pushMatrix();
        *///?} else if (>=1.20.1) {
        MatrixStack stack = drawContext.getMatrices();
        stack.push();
        //?}

        drawText(
                x, y,
                text, font
        );

        //? if (>=1.21.11) {
        /*stack.popMatrix();
        *///?} else if (>=1.20.1) {
        stack.pop();
        //?}
    }

    public void drawImage(VRenderContext ctx, int x, int y, int width, int height, Identifier path) {
        renderTexQuad(
                ctx.hasTransparency, path,
                x, y,
                x, y + height,
                x + width, y + height,
                x + width, y
        );
    }

    public void drawFill(VRenderContext ctx, int x, int y, int width, int height, VFill fill) {
        fill.renderQuad(ctx, x, y, width, height);
    }

    //
    // Basic rendering
    //

    public void drawRect(int x, int y, int width, int height, VColor color) {
        drawContext.fill(x, y, x + width, y + height, color.toIntArgb());
    }

    public void drawText(int x, int y, String text, VFont font) {
        float scaleFactor = font.getSize() / 16.0f;
        //? if (>=1.21.11) {
        /*drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(x, y);
        drawContext.getMatrices().scale(scaleFactor, scaleFactor);
        *///?} else if (>=1.20.1) {
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x, y, 0);
        drawContext.getMatrices().scale(scaleFactor, scaleFactor, 1.0f);
        //?}

        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(text).setStyle(
                        Style.EMPTY.withFont(
                                //? if (>=1.21.11) {
                                /*new StyleSpriteSource.Font(Identifier.of(font.getName()))
                                *///?} else if (>=1.21.1) {
                                /*Identifier.of(font.getName())
                                 *///? } else if (>=1.20.1) {
                                Identifier.tryParse(font.getName())
                                 //?}
                        )
                ),
                0, 0,
                font.getColor().toIntArgb(),
                false
        );

        //? if (>=1.21.11) {
        /*drawContext.getMatrices().popMatrix();
        *///?} else if (>=1.20.1) {
        drawContext.getMatrices().pop();
        //?}
    }

    public void drawImage(int x, int y, int width, int height, Identifier path) {
        //? if (>=1.21.11) {
        /*drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, path, x, y, 0, 0, width, height, width, height);
        *///?} else if (>=1.20.1) {
        drawContext.drawTexture(path, x, y, 0, 0, width, height, width, height);
        //?}
    }

    //
    // Low-level rendering
    //

    /**
     * Renders a solid-colored quad to the GUI render layer.
     *
     * <p>Vertices must be provided in counter-clockwise order in screen space
     * (Minecraft GUI coordinates, where Y increases downward):</p>
     *
     * <pre>
     * v1 ── v4
     * │     │
     * v2 ── v3
     * </pre>
     *
     * <ul>
     *   <li>v1 = top-left</li>
     *   <li>v2 = bottom-left</li>
     *   <li>v3 = bottom-right</li>
     *   <li>v4 = top-right</li>
     * </ul>
     *
     * <p>No validation or reordering is performed. Incorrect vertex order or
     * duplicated vertices will result in visual artifacts or no output.</p>
     *
     * <p>All vertices are rendered with the same color.</p>
     *
     * <p>This method renders using {@code RenderLayer.getGui()} and immediately
     * flushes the vertex buffer.</p>
     *
     * @param v1x   top-left x
     * @param v1y   top-left y
     * @param v2x   bottom-left x
     * @param v2y   bottom-left y
     * @param v3x   bottom-right x
     * @param v3y   bottom-right y
     * @param v4x   top-right x
     * @param v4y   top-right y
     * @param color color applied to all vertices
     */
    public void renderColQuad(
            int v1x, int v1y,
            int v2x, int v2y,
            int v3x, int v3y,
            int v4x, int v4y,
            VColor color
    ) {
        renderColQuad(v1x, v1y, color, v2x, v2y, color, v3x, v3y, color, v4x, v4y, color);
    }

    /**
     * Renders a quad to the GUI render layer with per-vertex colors.
     *
     * <p>Vertices must be provided in counter-clockwise order in screen space
     * (Minecraft GUI coordinates, where Y increases downward):</p>
     *
     * <pre>
     * v1 ── v4
     * │     │
     * v2 ── v3
     * </pre>
     *
     * <p>No validation or reordering is performed.</p>
     *
     * <p>This method renders using {@code RenderLayer.getGui()} and immediately
     * flushes the vertex buffer.</p>
     *
     * @param v1x   top-left x
     * @param v1y   top-left y
     * @param v1col color at v1
     * @param v2x   bottom-left x
     * @param v2y   bottom-left y
     * @param v2col color at v2
     * @param v3x   bottom-right x
     * @param v3y   bottom-right y
     * @param v3col color at v3
     * @param v4x   top-right x
     * @param v4y   top-right y
     * @param v4col color at v4
     */
    public void renderColQuad(
            int v1x, int v1y, VColor v1col,
            int v2x, int v2y, VColor v2col,
            int v3x, int v3y, VColor v3col,
            int v4x, int v4y, VColor v4col
    ) {
        //? if (>=1.21.11) {
        /*Window window = MinecraftClient.getInstance().getWindow();
        int w = window.getScaledWidth();
        int h = window.getScaledHeight();

        Matrix3x2f matrix = new Matrix3x2f(drawContext.getMatrices());

        ((DrawContextAccessor) drawContext).vera$getState().addSimpleElement(new SimpleGuiElementRenderState() {
            @Override
            public void setupVertices(VertexConsumer vertices) {
                org.joml.Vector2f v = new org.joml.Vector2f();

                matrix.transformPosition(v1x, v1y, v);
                vertices.vertex(v.x, v.y, 0f).color(v1col.toIntArgb());
                matrix.transformPosition(v2x, v2y, v);
                vertices.vertex(v.x, v.y, 0f).color(v2col.toIntArgb());
                matrix.transformPosition(v3x, v3y, v);
                vertices.vertex(v.x, v.y, 0f).color(v3col.toIntArgb());
                matrix.transformPosition(v4x, v4y, v);
                vertices.vertex(v.x, v.y, 0f).color(v4col.toIntArgb());
            }

            @Override
            public RenderPipeline pipeline() {
                return RenderPipelines.GUI;
            }

            @Override
            public TextureSetup textureSetup() {
                return TextureSetup.empty();
            }

            @Override
            public @Nullable ScreenRect scissorArea() {
                return null;
            }

            @Override
            public ScreenRect bounds() {
                return new ScreenRect(0, 0, w, h);
            }
        });
        *///?} else if (>=1.20.1) {
        Matrix4f matrix = drawContext.getMatrices().peek().getPositionMatrix();
        VertexConsumer consumer = drawContext.getVertexConsumers().getBuffer(RenderLayer.getGui());

        consumer.vertex(matrix, (float) v1x, (float) v1y, 0f).color(v1col.toIntArgb())
                //? if (1.20.1)
                .next()
                ;
        consumer.vertex(matrix, (float) v2x, (float) v2y, 0f).color(v2col.toIntArgb())
                //? if (1.20.1)
                .next()
                ;
        consumer.vertex(matrix, (float) v3x, (float) v3y, 0f).color(v3col.toIntArgb())
                //? if (1.20.1)
                 .next()
                ;
        consumer.vertex(matrix, (float) v4x, (float) v4y, 0f).color(v4col.toIntArgb())
                //? if (1.20.1)
                .next()
                ;

        ((DrawContextAccessor) drawContext).mcvera$invokeTryDraw();
        //?}
    }

    /**
     * Renders a textured quad to the GUI render layer using the full texture.
     *
     * <p>The texture is automatically bound via the provided
     * {@link net.minecraft.util.Identifier}.</p>
     *
     * <p>Blending is automatically enabled and disabled based on
     * {@code hasTransparentParts}.</p>
     *
     * <p>Vertices must be provided in counter-clockwise order in screen space
     * (Minecraft GUI coordinates, where Y increases downward):</p>
     *
     * <pre>
     * v1 ── v4
     * │     │
     * v2 ── v3
     * </pre>
     *
     * <p>Texture coordinates are automatically mapped to the full texture
     * (u,v in the range 0.0–1.0).</p>
     *
     * <p>The vertex buffer is flushed immediately.</p>
     *
     * @param hasTransparentParts whether the texture has transparent parts; handles blending
     * @param texture             texture identifier to bind
     * @param v1x                 top-left x
     * @param v1y                 top-left y
     * @param v2x                 bottom-left x
     * @param v2y                 bottom-left y
     * @param v3x                 bottom-right x
     * @param v3y                 bottom-right y
     * @param v4x                 top-right x
     * @param v4y                 top-right y
     */
    public void renderTexQuad(
            boolean hasTransparentParts,
            Identifier texture,
            int v1x, int v1y,
            int v2x, int v2y,
            int v3x, int v3y,
            int v4x, int v4y
    ) {
        VColor color = VColor.white();

        renderTexQuad(
                hasTransparentParts, texture, color,
                v1x, v1y, v2x, v2y, v3x, v3y, v4x, v4y
        );
    }

    /**
     * Renders a textured quad to the GUI render layer using the full texture.
     *
     * <p>The texture is automatically bound via the provided
     * {@link net.minecraft.util.Identifier}.</p>
     *
     * <p>Blending is automatically enabled and disabled based on
     * {@code hasTransparentParts}.</p>
     *
     * <p>Vertices must be provided in counter-clockwise order in screen space
     * (Minecraft GUI coordinates, where Y increases downward):</p>
     *
     * <pre>
     * v1 ── v4
     * │     │
     * v2 ── v3
     * </pre>
     *
     * <p>Texture coordinates are automatically mapped to the full texture
     * (u,v in the range 0.0–1.0).</p>
     *
     * <p>The vertex buffer is flushed immediately.</p>
     *
     * @param hasTransparentParts whether the texture has transparent parts; handles blending
     * @param texture             texture identifier to bind
     * @param color               tint to render with
     * @param v1x                 top-left x
     * @param v1y                 top-left y
     * @param v2x                 bottom-left x
     * @param v2y                 bottom-left y
     * @param v3x                 bottom-right x
     * @param v3y                 bottom-right y
     * @param v4x                 top-right x
     * @param v4y                 top-right y
     */
    public void renderTexQuad(
            boolean hasTransparentParts,
            Identifier texture,
            VColor color,
            int v1x, int v1y,
            int v2x, int v2y,
            int v3x, int v3y,
            int v4x, int v4y
    ) {
        renderTexQuad(
                hasTransparentParts, texture,
                v1x, v1y, 0.0f, 0.0f, color,
                v2x, v2y, 0.0f, 1.0f, color,
                v3x, v3y, 1.0f, 1.0f, color,
                v4x, v4y, 1.0f, 0.0f, color
        );
    }

    /**
     * Renders a textured quad to the GUI render layer with per-vertex UVs.
     *
     * <p>The texture is automatically bound via the provided
     * {@link net.minecraft.util.Identifier}.</p>
     *
     * <p>Blending is automatically enabled and disabled based on
     * {@code hasTransparentParts}.</p>
     *
     * <p>Vertices must be provided in counter-clockwise order in screen space
     * (Minecraft GUI coordinates, where Y increases downward).</p>
     *
     * <p>No validation or UV normalization is performed.</p>
     *
     * <p>The vertex buffer is flushed immediately.</p>
     *
     * @param hasTransparentParts whether the texture has transparent parts; handles blending
     * @param texture             texture identifier to bind
     * @param v1x                 top-left x
     * @param v1y                 top-left y
     * @param u1                  texture u at v1
     * @param v1t                 texture v at v1
     * @param v1c                 tint at v1
     * @param v2x                 bottom-left x
     * @param v2y                 bottom-left y
     * @param u2                  texture u at v2
     * @param v2t                 texture v at v2
     * @param v2c                 tint at v2
     * @param v3x                 bottom-right x
     * @param v3y                 bottom-right y
     * @param u3                  texture u at v3
     * @param v3t                 texture v at v3
     * @param v3c                 tint at v3
     * @param v4x                 top-right x
     * @param v4y                 top-right y
     * @param u4                  texture u at v4
     * @param v4t                 texture v at v4
     * @param v4c                 tint at v4
     */
    public void renderTexQuad(
            boolean hasTransparentParts,
            Identifier texture,
            int v1x, int v1y, float u1, float v1t, VColor v1c,
            int v2x, int v2y, float u2, float v2t, VColor v2c,
            int v3x, int v3y, float u3, float v3t, VColor v3c,
            int v4x, int v4y, float u4, float v4t, VColor v4c
    ) {
        //? if (>=1.21.11) {
        /*Window window = MinecraftClient.getInstance().getWindow();
        int w = window.getScaledWidth();
        int h = window.getScaledHeight();

        Matrix3x2f matrix = new Matrix3x2f(drawContext.getMatrices());

        ((DrawContextAccessor) drawContext).vera$getState().addSimpleElement(new SimpleGuiElementRenderState() {
            @Override
            public void setupVertices(VertexConsumer vertices) {
                org.joml.Vector2f v = new org.joml.Vector2f();

                matrix.transformPosition(v1x, v1y, v);
                vertices.vertex(v.x, v.y, 0f).texture(u1, v1t).color(v1c.toIntArgb());
                matrix.transformPosition(v2x, v2y, v);
                vertices.vertex(v.x, v.y, 0f).texture(u2, v2t).color(v2c.toIntArgb());
                matrix.transformPosition(v3x, v3y, v);
                vertices.vertex(v.x, v.y, 0f).texture(u3, v3t).color(v3c.toIntArgb());
                matrix.transformPosition(v4x, v4y, v);
                vertices.vertex(v.x, v.y, 0f).texture(u4, v4t).color(v4c.toIntArgb());
            }

            @Override
            public RenderPipeline pipeline() {
                return RenderPipelines.GUI_TEXTURED;
            }

            @Override
            public TextureSetup textureSetup() {
                TextureManager tm = MinecraftClient.getInstance().getTextureManager();
                AbstractTexture tex = tm.getTexture(texture);
                GpuTextureView view = tex.getGlTextureView();
                GpuSampler sampler = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);
                return TextureSetup.of(view, sampler);
            }

            @Override
            public @Nullable ScreenRect scissorArea() {
                return null;
            }

            @Override
            public ScreenRect bounds() {
                return new ScreenRect(0, 0, w, h);
            }
        });
        *///?}

        //? if (>=1.21.1 && <1.21.11) {
        /*RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        *///? } else if (>=1.20.1 && <1.21.11) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        //? }

        //? if (>=1.20.1 && <1.21.11) {
        if (hasTransparentParts) RenderSystem.enableBlend();
        Matrix4f matrix = drawContext.getMatrices().peek().getPositionMatrix();
        //?}

        //? if (>=1.21.1 && <1.21.11) {
        /*BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        *///? } else if (>=1.20.1 && <1.21.11) {
        BufferBuilder buf = Tessellator.getInstance().getBuffer();
        buf.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        //? }

        //? if (>=1.20.1 && <1.21.11) {
        buf.vertex(matrix, v1x, v1y, 0f).color(v1c.toIntArgb()).texture(u1, v1t)
                //? if (>=1.20.1 && <1.21.1)
                .next()
                ;
        buf.vertex(matrix, v2x, v2y, 0f).color(v2c.toIntArgb()).texture(u2, v2t)
                //? if (>=1.20.1 && <1.21.1)
                .next()
                ;
        buf.vertex(matrix, v3x, v3y, 0f).color(v3c.toIntArgb()).texture(u3, v3t)
                //? if (>=1.20.1 && <1.21.1)
                .next()
                ;
        buf.vertex(matrix, v4x, v4y, 0f).color(v4c.toIntArgb()).texture(u4, v4t)
                //? if (>=1.20.1 && <1.21.1)
                .next()
                ;

        BufferRenderer.drawWithGlobalProgram(buf.end());
        if (hasTransparentParts) RenderSystem.disableBlend();
        //?}
    }

    //
    // Apps
    //

    public void ensureClearContext(VRenderContext ctx) {
        if (ctx.peekClip() != null) {
            throw new RuntimeException("Unclosed clip stack");
        }
    }

    public void renderApp(VeraApp app) {
        boolean blendEnabled = GL11.glIsEnabled(GL_BLEND);

        List<VWidget<?>> widgets = app.getWidgets();
        //? if (>=1.21.11) {
        /*if (!blendEnabled) GL11.glEnable(GL_BLEND);
        *///?} else if (>=1.20.1) {
        if (!blendEnabled) RenderSystem.enableBlend();
        //?}

        app.render();
        VWidget<?> hoveredWidget = !app.hasFlag(VAppFlag.HIERARCHIC) || MCVeraData.isTopHierarchy(app)
                ? app.getTopWidgetAt(Vera.getMouseX(), Vera.getMouseY())
                : null;

        for (VWidget<?> widget : widgets) {
            if (widget != hoveredWidget && widget.isHovered()) widget.setHovered(false);
            else if (widget == hoveredWidget && !widget.isHovered()) widget.setHovered(true);

            widget.renderSelf();
        }
        app.renderAfterWidgets();

        if (app.hasFlag(VAppFlag.HIERARCHIC) && !MCVeraData.isTopHierarchy(app)) app.renderHierarchyOverlay();

        //? if (>=1.21.11) {
        /*if (!blendEnabled) GL11.glDisable(GL_BLEND);
        *///?} else if (>=1.20.1) {
        if (!blendEnabled) RenderSystem.disableBlend();
         //?}
    }

    public void renderApps(VAppPositioningFlag flag) {
        if (!MinecraftClient.getInstance().isRunning()) return;

        LinkedHashSet<VeraApp> apps = MCVeraData.visibleApplications.getOrDefault(flag, new LinkedHashSet<>());

        for (VeraApp app : apps) {
            if (app.hasFlag(VAppFlag.HIERARCHIC)) continue;
            Vera.renderer.renderApp(app);
        }

        List<VeraApp> hierarchicApps = new ArrayList<>(MCVeraData.getAppsWithFlag(VAppFlag.HIERARCHIC));
        Collections.reverse(hierarchicApps);
        for (VeraApp app : hierarchicApps) {
            if (app.getPositioning() != flag || !MCVeraData.visibleApplications.get(app.getPositioning()).contains(app)) {
                continue;
            }
            Vera.renderer.renderApp(app);
        }
    }
}
