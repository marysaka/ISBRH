package eu.thog92.isbrh.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;


/**
 * A simple class that add quads rendering (based on 1.7 RenderBlocks)
 */
public class SimpleBlockRender
{

    public double renderMinX, renderMaxX, renderMinY, renderMaxY, renderMinZ,
            renderMaxZ;

    public int uvRotateEast, uvRotateWest, uvRotateSouth, uvRotateNorth,
            uvRotateTop, uvRotateBottom;
    ;

    public WorldRenderer worldRenderer;

    public boolean renderFromInside, flipTexture;

    /**
     * RGB colors by side
     */
    public float colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft,
            colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft,
            colorRedTopRight, colorGreenTopRight, colorBlueTopRight,
            colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight;

    /**
     * Brightness by side
     */
    public int brightnessTopLeft, brightnessBottomLeft, brightnessBottomRight,
            brightnessTopRight;

    public boolean enableAO;
    public boolean renderAllFaces;
    private Minecraft minecraft;
    private World world;

    public SimpleBlockRender()
    {
        this.renderMaxX = 1.0;
        this.renderMaxY = 1.0;
        this.renderMaxZ = 1.0;
        this.minecraft = Minecraft.getMinecraft();
    }

    public SimpleBlockRender(WorldRenderer renderer)
    {
        this();
        this.worldRenderer = renderer;
    }

    public void setRenderFromInside(boolean value)
    {
        this.renderFromInside = value;
    }

    public void setRenderBounds(double minX, double minY, double minZ,
                                double maxX, double maxY, double maxZ)
    {
        this.renderMinX = minX;
        this.renderMaxX = maxX;
        this.renderMinY = minY;
        this.renderMaxY = maxY;
        this.renderMinZ = minZ;
        this.renderMaxZ = maxZ;
    }

    public WorldRenderer applyRenderCallback(WorldRenderer renderer, RenderCallback callback)
    {
        return callback.beforeFinishVertex(renderer);
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: block,
     * x, y, z, texture
     */
    public void renderFaceYNeg(double x, double y, double z,
                               TextureAtlasSprite texture, RenderCallback callback)
    {
        if (callback == null)
            callback = new RenderCallback()
            {
                @Override
                public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                {
                    return renderer;
                }
            };
        double d3 = (double) texture.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) texture.getInterpolatedU(this.renderMaxX * 16.0D);
        double d5 = (double) texture.getInterpolatedV(this.renderMinZ * 16.0D);
        double d6 = (double) texture.getInterpolatedV(this.renderMaxZ * 16.0D);

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D)
        {
            d3 = (double) texture.getMinU();
            d4 = (double) texture.getMaxU();
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D)
        {
            d5 = (double) texture.getMinV();
            d6 = (double) texture.getMaxV();
        }

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateBottom == 2)
        {
            d3 = (double) texture.getInterpolatedU(this.renderMinZ * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d4 = (double) texture.getInterpolatedU(this.renderMaxZ * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateBottom == 1)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateBottom == 3)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = x + this.renderMinX;
        double d12 = x + this.renderMaxX;
        double d13 = y + this.renderMinY;
        double d14 = z + this.renderMinZ;
        double d15 = z + this.renderMaxZ;

        if (this.renderFromInside)
        {
            d11 = x + this.renderMaxX;
            d12 = x + this.renderMinX;
        }
        if (this.enableAO)
        {
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d4, d6).color(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft, 255F).lightmap(brightnessTopLeft >> 16 & 65535, brightnessTopLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d14).tex(d7, d9).color(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft, 255F).lightmap(brightnessBottomLeft >> 16 & 65535, brightnessBottomLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d3, d5).color(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight, 255F).lightmap(brightnessBottomRight >> 16 & 65535, brightnessBottomRight & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d8, d10).color(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight, 255F).lightmap(brightnessTopRight >> 16 & 65535, brightnessTopRight & 65535)).endVertex();
        } else
        {
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d4, d6)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d14).tex(d7, d9)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d3, d5)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d8, d10)).endVertex();
        }

    }

    /**
     * Renders the given texture to the top face of the block. Args: x, y, z,
     * texture
     */
    public void renderFaceYPos(double x, double y, double z,
                               TextureAtlasSprite texture, RenderCallback callback)
    {

        double d3 = (double) texture.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) texture.getInterpolatedU(this.renderMaxX * 16.0D);
        double d5 = (double) texture.getInterpolatedV(this.renderMinZ * 16.0D);
        double d6 = (double) texture.getInterpolatedV(this.renderMaxZ * 16.0D);

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D)
        {
            d3 = (double) texture.getMinU();
            d4 = (double) texture.getMaxU();
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D)
        {
            d5 = (double) texture.getMinV();
            d6 = (double) texture.getMaxV();
        }

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateTop == 1)
        {
            d3 = (double) texture.getInterpolatedU(this.renderMinZ * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d4 = (double) texture.getInterpolatedU(this.renderMaxZ * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateTop == 2)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateTop == 3)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = x + this.renderMinX;
        double d12 = x + this.renderMaxX;
        double d13 = y + this.renderMaxY;
        double d14 = z + this.renderMinZ;
        double d15 = z + this.renderMaxZ;

        if (this.renderFromInside)
        {
            d11 = x + this.renderMaxX;
            d12 = x + this.renderMinX;
        }

        if (this.enableAO)
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d8, d10).color(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft, 255F).lightmap(brightnessTopLeft >> 16 & 65535, brightnessTopLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d3, d5).color(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft, 255F).lightmap(brightnessBottomLeft >> 16 & 65535, brightnessBottomLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d14).tex(d7, d9).color(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight, 255F).lightmap(brightnessBottomRight >> 16 & 65535, brightnessBottomRight & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d4, d6).color(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight, 255F).lightmap(brightnessTopRight >> 16 & 65535, brightnessTopRight & 65535)).endVertex();
        } else
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d8, d10)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d3, d5)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d14).tex(d7, d9)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d4, d6)).endVertex();
        }

    }

    /**
     * Renders the given texture to the north (z-negative) face of the block.
     * Args: x, y, z, texture
     */
    public void renderFaceZNeg(double x, double y, double z,
                               TextureAtlasSprite texture, RenderCallback callback)
    {

        double d3 = (double) texture.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) texture.getInterpolatedU(this.renderMaxX * 16.0D);

        double d5 = (double) texture
                .getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) texture
                .getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture)
        {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D)
        {
            d3 = (double) texture.getMinU();
            d4 = (double) texture.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D)
        {
            d5 = (double) texture.getMinV();
            d6 = (double) texture.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateEast == 2)
        {
            d3 = (double) texture.getInterpolatedU(this.renderMinY * 16.0D);
            d4 = (double) texture.getInterpolatedU(this.renderMaxY * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateEast == 1)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMaxX * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMinX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateEast == 3)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = x + this.renderMinX;
        double d12 = x + this.renderMaxX;
        double d13 = y + this.renderMinY;
        double d14 = y + this.renderMaxY;
        double d15 = z + this.renderMinZ;

        if (this.renderFromInside)
        {
            d11 = x + this.renderMaxX;
            d12 = x + this.renderMinX;
        }
        if (this.enableAO)
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d14, d15).tex(d7, d9).color(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft, 255F).lightmap(brightnessTopLeft >> 16 & 65535, brightnessTopLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d14, d15).tex(d3, d5).color(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft, 255F).lightmap(brightnessBottomLeft >> 16 & 65535, brightnessBottomLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d8, d10).color(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight, 255F).lightmap(brightnessBottomRight >> 16 & 65535, brightnessBottomRight & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d4, d6).color(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight, 255F).lightmap(brightnessTopRight >> 16 & 65535, brightnessTopRight & 65535)).endVertex();
        } else
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d14, d15).tex(d7, d9)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d14, d15).tex(d3, d5)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d8, d10)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d4, d6)).endVertex();
        }

    }

    /**
     * Renders the given texture to the south (z-positive) face of the block.
     * Args: x, y, z, texture
     */
    public void renderFaceZPos(double x, double y, double z,
                               TextureAtlasSprite texture, RenderCallback callback)
    {

        double d3 = (double) texture.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) texture.getInterpolatedU(this.renderMaxX * 16.0D);
        double d5 = (double) texture
                .getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) texture
                .getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture)
        {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D)
        {
            d3 = (double) texture.getMinU();
            d4 = (double) texture.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D)
        {
            d5 = (double) texture.getMinV();
            d6 = (double) texture.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateWest == 1)
        {
            d3 = (double) texture.getInterpolatedU(this.renderMinY * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d4 = (double) texture.getInterpolatedU(this.renderMaxY * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateWest == 2)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateWest == 3)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = x + this.renderMinX;
        double d12 = x + this.renderMaxX;
        double d13 = y + this.renderMinY;
        double d14 = y + this.renderMaxY;
        double d15 = z + this.renderMaxZ;

        if (this.renderFromInside)
        {
            d11 = x + this.renderMaxX;
            d12 = x + this.renderMinX;
        }
        if (this.enableAO)
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d14, d15).tex(d3, d5).color(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft, 255F).lightmap(brightnessTopLeft >> 16 & 65535, brightnessTopLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d8, d10).color(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft, 255F).lightmap(brightnessBottomLeft >> 16 & 65535, brightnessBottomLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d4, d6).color(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight, 255F).lightmap(brightnessBottomRight >> 16 & 65535, brightnessBottomRight & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d14, d15).tex(d7, d9).color(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight, 255F).lightmap(brightnessTopRight >> 16 & 65535, brightnessTopRight & 65535)).endVertex();
        } else
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d14, d15).tex(d3, d5)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d8, d10)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d13, d15).tex(d4, d6)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d12, d14, d15).tex(d7, d9)).endVertex();

        }

    }

    /**
     * Renders the given texture to the west (x-negative) face of the block.
     * Args: x, y, z, texture
     */
    public void renderFaceXNeg(double x, double y, double z,
                               TextureAtlasSprite texture, RenderCallback callback)
    {

        double d3 = (double) texture.getInterpolatedU(this.renderMinZ * 16.0D);
        double d4 = (double) texture.getInterpolatedU(this.renderMaxZ * 16.0D);
        double d5 = (double) texture
                .getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) texture
                .getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture)
        {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D)
        {
            d3 = (double) texture.getMinU();
            d4 = (double) texture.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D)
        {
            d5 = (double) texture.getMinV();
            d6 = (double) texture.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateNorth == 1)
        {
            d3 = (double) texture.getInterpolatedU(this.renderMinY * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d4 = (double) texture.getInterpolatedU(this.renderMaxY * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateNorth == 2)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMinZ * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateNorth == 3)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = x + this.renderMinX;
        double d12 = y + this.renderMinY;
        double d13 = y + this.renderMaxY;
        double d14 = z + this.renderMinZ;
        double d15 = z + this.renderMaxZ;

        if (this.renderFromInside)
        {
            d14 = z + this.renderMaxZ;
            d15 = z + this.renderMinZ;
        }

        if (this.enableAO)
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d7, d9).color(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft, 255F).lightmap(brightnessTopLeft >> 16 & 65535, brightnessTopLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d3, d5).color(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft, 255F).lightmap(brightnessBottomLeft >> 16 & 65535, brightnessBottomLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d14).tex(d8, d10).color(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight, 255F).lightmap(brightnessBottomRight >> 16 & 65535, brightnessBottomRight & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d15).tex(d4, d6).color(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight, 255F).lightmap(brightnessTopRight >> 16 & 65535, brightnessTopRight & 65535)).endVertex();
        } else
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d7, d9)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d3, d5)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d14).tex(d8, d10)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d15).tex(d4, d6)).endVertex();
        }
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block.
     * Args: x, y, z, texture
     */
    public void renderFaceXPos(double x, double y, double z,
                               TextureAtlasSprite texture, RenderCallback callback)
    {

        double d3 = (double) texture.getInterpolatedU(this.renderMinZ * 16.0D);
        double d4 = (double) texture.getInterpolatedU(this.renderMaxZ * 16.0D);
        double d5 = (double) texture
                .getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) texture
                .getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture)
        {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D)
        {
            d3 = (double) texture.getMinU();
            d4 = (double) texture.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D)
        {
            d5 = (double) texture.getMinV();
            d6 = (double) texture.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateSouth == 2)
        {
            d3 = (double) texture.getInterpolatedU(this.renderMinY * 16.0D);
            d5 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d4 = (double) texture.getInterpolatedU(this.renderMaxY * 16.0D);
            d6 = (double) texture
                    .getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateSouth == 1)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMaxZ * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMinZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateSouth == 3)
        {
            d3 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d4 = (double) texture
                    .getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) texture.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) texture.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = x + this.renderMaxX;
        double d12 = y + this.renderMinY;
        double d13 = y + this.renderMaxY;
        double d14 = z + this.renderMinZ;
        double d15 = z + this.renderMaxZ;

        if (this.renderFromInside)
        {
            d14 = z + this.renderMaxZ;
            d15 = z + this.renderMinZ;
        }

        if (this.enableAO)
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d15).tex(d8, d10).color(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft, 255F).lightmap(brightnessTopLeft >> 16 & 65535, brightnessTopLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d14).tex(d4, d6).color(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft, 255F).lightmap(brightnessBottomLeft >> 16 & 65535, brightnessBottomLeft & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d7, d9).color(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight, 255F).lightmap(brightnessBottomRight >> 16 & 65535, brightnessBottomRight & 65535)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d3, d5).color(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight, 255F).lightmap(brightnessTopRight >> 16 & 65535, brightnessTopRight & 65535)).endVertex();
        } else
        {
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d15).tex(d8, d10)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d12, d14).tex(d4, d6)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d14).tex(d7, d9)).endVertex();
            callback.beforeFinishVertex(worldRenderer.pos(d11, d13, d15).tex(d3, d5)).endVertex();
        }
    }

    public boolean renderInventoryStandardBlock(
            ITextureHandler textureManager, Tessellator tessellator)
    {
        IBlockState state = Blocks.air.getDefaultState();
        if (textureManager instanceof Block)
            state = ((Block) textureManager).getDefaultState();

        return this.renderInventoryStandardBlock(textureManager, state, tessellator);
    }

    public boolean renderInventoryStandardBlock(
            ITextureHandler textureManager, IBlockState state, Tessellator tessellator)
    {
        this.renderFromInside = true;
        // Inside Render
        worldRenderer.begin(7, DefaultVertexFormats.ITEM);
        this.renderFaceYNeg(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.DOWN), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, -1.0F, 0.0F);
                    }
                });
        this.renderFaceYPos(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.UP), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, 1.0F, 0.0F);
                    }
                });
        this.renderFaceZNeg(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.NORTH), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, 0.0F, -1.0F);
                    }
                });
        this.renderFaceZPos(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.SOUTH), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, 0.0F, 1.0F);
                    }
                });
        this.renderFaceXNeg(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.WEST), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(-1.0F, 0.0F, 0.0F);
                    }
                });
        this.renderFaceXPos(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.EAST), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(1.0F, 0.0F, 0.0F);
                    }
                });
        tessellator.draw();
        this.renderFromInside = false;
        // Normal Render
        worldRenderer.begin(7, DefaultVertexFormats.ITEM);
        this.renderFaceYNeg(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.DOWN), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, -1.0F, 0.0F);
                    }
                });
        this.renderFaceYPos(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.UP), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, 1.0F, 0.0F);
                    }
                });
        this.renderFaceZNeg(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.NORTH), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, 0.0F, -1.0F);
                    }
                });
        this.renderFaceZPos(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.SOUTH), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(0.0F, 0.0F, 1.0F);
                    }
                });
        this.renderFaceXNeg(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.WEST), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(-1.0F, 0.0F, 0.0F);
                    }
                });
        this.renderFaceXPos(0.0D, 0.0D, 0.0D,
                textureManager.getSidedTexture(state, EnumFacing.EAST), new RenderCallback()
                {
                    @Override
                    public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                    {
                        return renderer.normal(1.0F, 0.0F, 0.0F);
                    }
                });
        tessellator.draw();
        return true;
    }


    /**
     * Renders a standard cube block at the given coordinates.
     * Args: textureManager, pos
     */
    public boolean renderStandardBlock(ITextureHandler textureManager,
                                       BlockPos pos)
    {
        if (world == null)
            world = minecraft.theWorld;
        Block block = this.world.getBlockState(pos).getBlock();
        int l = this.world.getBlockState(pos).getBlock()
                .colorMultiplier(this.world, pos);
        float f = (float) (l >> 16 & 255) / 255.0F;
        float f1 = (float) (l >> 8 & 255) / 255.0F;
        float f2 = (float) (l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        this.renderFromInside = true;
        boolean inside = this.renderStandardBlockWithColorMultiplier(block, textureManager, pos, f, f1,
                f2);
        this.renderFromInside = false;
        return inside && this.renderStandardBlockWithColorMultiplier(block, textureManager, pos,
                f, f1, f2);
    }

    /**
     * Renders a standard cube block at the given coordinates, with a given
     * color ratio. Args: block, textureManager, pos, r, g, b
     */
    public boolean renderStandardBlockWithColorMultiplier(final Block block,
                                                          ITextureHandler textureManager, final BlockPos pos, float r,
                                                          float g, float b)
    {
        if (world == null)
            world = minecraft.theWorld;
        this.enableAO = false;
        boolean flag = false;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        final float f7 = f4 * r;
        final float f8 = f4 * g;
        final float f9 = f4 * b;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        if (block != Blocks.grass)
        {
            f10 = f3 * r;
            f11 = f5 * r;
            f12 = f6 * r;
            f13 = f3 * g;
            f14 = f5 * g;
            f15 = f6 * g;
            f16 = f3 * b;
            f17 = f5 * b;
            f18 = f6 * b;
        }

        final int l = block.getMixedBrightnessForBlock(world, pos);
        IBlockState state = world.getBlockState(pos);

        if (this.renderAllFaces
                || block.shouldSideBeRendered(world, pos.down(),
                EnumFacing.DOWN))
        {
            final float redColor = f10;
            final float blueColor = f13;
            final float greenColor = f16;
            this.renderFaceYNeg(pos.getX(), pos.getY(), pos.getZ(),
                    textureManager.getSidedTexture(state, EnumFacing.DOWN), new RenderCallback()
                    {
                        @Override
                        public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                        {
                            int bright = renderMinY > 0.0D ? l : block
                                    .getMixedBrightnessForBlock(world, pos.down());
                            return renderer.color(redColor, blueColor, greenColor, 255F).lightmap(bright >> 16 & 65535, bright & 65535);
                        }
                    });
            flag = true;
        }

        if (this.renderAllFaces
                || block.shouldSideBeRendered(world, pos.up(), EnumFacing.UP))
        {
            this.renderFaceYPos(pos.getX(), pos.getY(), pos.getZ(),
                    textureManager.getSidedTexture(state, EnumFacing.UP), new RenderCallback()
                    {
                        @Override
                        public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                        {
                            int bright = renderMaxY < 1.0D ? l : block
                                    .getMixedBrightnessForBlock(world, pos.up());
                            return renderer.color(f7, f8, f9, 255F).lightmap(bright >> 16 & 65535, bright & 65535);
                        }
                    });
            flag = true;
        }

        if (this.renderAllFaces
                || block.shouldSideBeRendered(world, pos.north(),
                EnumFacing.NORTH))
        {
            final float redColor = f11;
            final float blueColor = f14;
            final float greenColor = f17;
            this.renderFaceZNeg(pos.getX(), pos.getY(), pos.getZ(),
                    textureManager.getSidedTexture(state, EnumFacing.NORTH), new RenderCallback()
                    {
                        @Override
                        public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                        {
                            int bright = renderMinZ > 0.0D ? l : block
                                    .getMixedBrightnessForBlock(world, pos.north());
                            return renderer.color(redColor, blueColor, greenColor, 255F).lightmap(bright >> 16 & 65535, bright & 65535);
                        }
                    });

            flag = true;
        }

        if (this.renderAllFaces
                || block.shouldSideBeRendered(world, pos.south(),
                EnumFacing.SOUTH))
        {
            final float redColor = f11;
            final float blueColor = f14;
            final float greenColor = f17;
            this.renderFaceZPos(pos.getX(), pos.getY(), pos.getZ(),
                    textureManager.getSidedTexture(state, EnumFacing.SOUTH), new RenderCallback()
                    {
                        @Override
                        public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                        {
                            int bright = renderMaxZ < 1.0D ? l : block
                                    .getMixedBrightnessForBlock(world, pos.south());
                            return renderer.color(redColor, blueColor, greenColor, 255F).lightmap(bright >> 16 & 65535, bright & 65535);
                        }
                    });

            flag = true;
        }

        if (this.renderAllFaces
                || block.shouldSideBeRendered(world, pos.west(),
                EnumFacing.WEST))
        {
            final float redColor = f12;
            final float blueColor = f15;
            final float greenColor = f18;

            this.renderFaceXNeg(pos.getX(), pos.getY(), pos.getZ(),
                    textureManager.getSidedTexture(state, EnumFacing.WEST), new RenderCallback()
                    {
                        @Override
                        public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                        {
                            int bright = renderMinX > 0.0D ? l : block
                                    .getMixedBrightnessForBlock(world, pos.west());
                            return renderer.color(redColor, blueColor, greenColor, 255F).lightmap(bright >> 16 & 65535, bright & 65535);
                        }
                    });

            flag = true;
        }

        if (this.renderAllFaces
                || block.shouldSideBeRendered(world, pos.east(),
                EnumFacing.EAST))
        {
            final float redColor = f12;
            final float blueColor = f15;
            final float greenColor = f18;
            this.renderFaceXPos(pos.getX(), pos.getY(), pos.getZ(),
                    textureManager.getSidedTexture(state, EnumFacing.EAST), new RenderCallback()
                    {
                        @Override
                        public WorldRenderer beforeFinishVertex(WorldRenderer renderer)
                        {
                            int bright = renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(world, pos.east());
                            return renderer.color(redColor, blueColor, greenColor, 255F).lightmap(bright >> 16 & 65535, bright & 65535);
                        }
                    });

            flag = true;
        }

        return flag;
    }

    public static class RenderCallback<E extends WorldRenderer>
    {
        public E beforeFinishVertex(E renderer)
        {
            return renderer;
        }
    }

}
