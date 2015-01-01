package eu.thog92.isbrh.coremod;

import eu.thog92.isbrh.registry.RenderRegistry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


/**
 * Internal Class for methods than have been overrides
 * Will be remove sooner
 */
@Deprecated
public class RenderAccessHook {

    @Deprecated
    public static void renderItemBody(RenderItem renderItem, ItemStack stack,
                                      IBakedModel model, ItemCameraTransforms.TransformType transformType) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);

        if (model.isBuiltInRenderer()) {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityItemStackRenderer.instance.renderByItem(stack);
        } else if (stack.getItem() instanceof ItemBlock
                && ((ItemBlock) stack.getItem()).getBlock().getRenderType() > 3) {
            RenderRegistry.instance().renderInventoryBlock(stack, transformType);
        } else {
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            renderItem.renderModel(model, stack);

            if (stack.hasEffect()) {
                renderItem.renderEffect(model);
            }
        }

        GlStateManager.popMatrix();
    }

    @Deprecated
    public static boolean shouldRenderItemIn3DBody(RenderItem renderItem,
                                                   ItemStack stack) {
        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(
                stack);
        if (ibakedmodel == null
                || ibakedmodel == renderItem.getItemModelMesher()
                .getModelManager().getMissingModel())
            return RenderRegistry.instance().shouldRender3DInInventory(stack);

        return ibakedmodel.isGui3d();
    }
}
