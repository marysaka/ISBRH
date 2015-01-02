package eu.thog92.isbrh.coremod;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import eu.thog92.isbrh.registry.RenderRegistry;


/**
 * Internal Class for methods than have been overrides
 * Will be remove sooner
 */
@Deprecated
public class RenderAccessHook {

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
    
    public void test(int i, int y)
    {
        if(i > y)
            shouldRenderItemIn3DBody(null, null);
        else
            i = y;
    }
}
