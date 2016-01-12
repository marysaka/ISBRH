package eu.thog92.isbrh;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import eu.thog92.isbrh.registry.RenderRegistry;

public class RenderItemISBRH extends RenderItem
{

    protected TextureManager textureManager;
    private final RenderItem wrapped;

    public RenderItemISBRH(TextureManager textureManager,
            ModelManager modelManager, RenderItem renderItem)
    {
        super(textureManager, modelManager);
        this.textureManager = textureManager;
        this.wrapped = renderItem;
        try
        {
            Field mesher = ReflectionHelper.findField(RenderItem.class, "m", "field_175059_m", "itemModelMesher");
            mesher.setAccessible(true);
            mesher.set(this, renderItem.getItemModelMesher());
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void registerBlock(Block blk, String identifier)
    {
        wrapped.registerBlock(blk, identifier);
    }

    @Override
    public void registerItem(Item itm, String identifier)
    {
        wrapped.registerItem(itm, identifier);
    }

    @Override
    public void registerBlock(Block blk, int subType, String identifier)
    {
        wrapped.registerBlock(blk, subType, identifier);
    }

    @Override
    public void registerItem(Item itm, int subType, String identifier)
    {
        wrapped.registerItem(itm, subType, identifier);
    }

    @Override
    public ItemModelMesher getItemModelMesher()
    {
        return wrapped.getItemModelMesher();
    }

    @Override
    public void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d)
    {
        wrapped.setupGuiTransform(xPosition, yPosition, isGui3d);
    }

    @Override
    public void renderQuads(WorldRenderer renderer, List quads, int color, ItemStack stack)
    {
        wrapped.renderQuads(renderer, quads, color, stack);
    }

    @Override
    public void renderQuad(WorldRenderer renderer, BakedQuad quad, int color)
    {
        wrapped.renderQuad(renderer, quad, color);
    }

    @Override
    public void renderEffect(IBakedModel model)
    {
        wrapped.renderEffect(model);
    }

    @Override
    public void renderItemModel(ItemStack stack)
    {
        wrapped.renderItemModel(stack);
    }

    @Override
    public void renderModel(IBakedModel model, int color)
    {
        wrapped.renderModel(model, color);
    }

    @Override
    public void renderModel(IBakedModel model, int color, ItemStack stack)
    {
        wrapped.renderModel(model, color, stack);
    }

    @Override
    public void renderModel(IBakedModel model, ItemStack stack)
    {
        wrapped.renderModel(model, stack);
    }

    @Override
    public void putQuadNormal(WorldRenderer renderer, BakedQuad quad)
    {
        wrapped.putQuadNormal(renderer, quad);
    }

    @Override
    public void applyTransform(ItemTransformVec3f transform)
    {
        wrapped.applyTransform(transform);
    }

    @Override
    public void preTransform(ItemStack stack)
    {
        wrapped.preTransform(stack);
    }

    @Override
    public void drawRect(WorldRenderer renderer, int x, int y, int width, int height, int color)
    {
        wrapped.drawRect(renderer, x, y, width, height, color);
    }

    @Override
    public void func_175039_a(boolean p_175039_1_)
    {
        wrapped.func_175039_a(p_175039_1_);
    }

    private boolean isISBRH(ItemStack stack)
    {
        return (((stack.getItem() instanceof ItemBlock))
            && (((ItemBlock) stack.getItem()).getBlock()
            .getRenderType() > 4));
    }

    @Override
    public void renderItem(ItemStack stack, IBakedModel model)
    {
        if (isISBRH(stack))
            this.renderItem(stack, TransformType.NONE);
        else
            wrapped.renderItem(stack, model);
    }

    @Override
    public void renderItemModelTransform(ItemStack stack, IBakedModel model,
            ItemCameraTransforms.TransformType cameraTransformType)
    {
        if (isISBRH(stack))
        {
            this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
            this.textureManager.getTexture(TextureMap.locationBlocksTexture)
                    .setBlurMipmap(false, false);
            this.preTransform(stack);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            renderItem(stack, cameraTransformType);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
            this.textureManager.getTexture(TextureMap.locationBlocksTexture)
                    .restoreLastBlurMipmap();
        } else
            wrapped.renderItemModelTransform(stack, model, cameraTransformType);
    }

    @Override
    public void renderItemIntoGUI(ItemStack stack, int x, int y)
    {
        if (isISBRH(stack))
        {
            IBakedModel ibakedmodel = this.getItemModelMesher().getItemModel(stack);
            GlStateManager.pushMatrix();
            this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
            this.textureManager.getTexture(TextureMap.locationBlocksTexture)
                    .setBlurMipmap(false, false);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            setupGuiTransform(x, y, ibakedmodel.isGui3d());
            applyTransform(ibakedmodel.getItemCameraTransforms().gui);
            renderItem(stack, ItemCameraTransforms.TransformType.GUI);
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
            this.textureManager.getTexture(TextureMap.locationBlocksTexture)
                    .restoreLastBlurMipmap();
        }
        else
        {
            wrapped.renderItemIntoGUI(stack, x, y);
        }
    }

    private void renderItem(ItemStack paramItemStack, ItemCameraTransforms.TransformType paramTransformType)
    {
        RenderRegistry.instance().renderInventoryBlock(paramItemStack, paramTransformType);
    }

    @Override
    public boolean shouldRenderItemIn3D(ItemStack stack)
    {
        if (isISBRH(stack))
            return RenderRegistry.instance().shouldRender3DInInventory(stack);

        return wrapped.shouldRenderItemIn3D(stack);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        wrapped.onResourceManagerReload(resourceManager);
    }


    @Override
    public void registerItems()
    {

    }
}
