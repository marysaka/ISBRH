package eu.thog92.isbrh.registry;

import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.google.common.collect.Maps;

import eu.thog92.isbrh.render.ISimpleBlockRenderingHandler;
import eu.thog92.isbrh.render.TextureLoader;

public class RenderRegistry {

	private static final RenderRegistry INSTANCE = new RenderRegistry();

	private int nextRenderId = 4;

	private Map<Integer, ISimpleBlockRenderingHandler> renders = Maps
			.newHashMap();

	private TextureLoader loader = new TextureLoader();

	public static RenderRegistry instance() {
		return INSTANCE;
	}

	/**
	 * Get the next available renderId from the block render ID list
	 */
	public static int getNextAvailableRenderId() {
		return instance().nextRenderId++;
	}

	/**
	 * Register a simple block rendering handler
	 *
	 * @param handler
	 */
	public static void registerBlockHandler(ISimpleBlockRenderingHandler handler) {
		instance().renders.put(handler.getRenderId(), handler);
	}

	/**
	 * Register the simple block rendering handler This version will not call
	 * getRenderId on the passed in handler, instead using the supplied ID, so
	 * you can easily re-use the same rendering handler for multiple IDs
	 *
	 * @param renderId
	 * @param handler
	 */
	public static void registerBlockHandler(int renderId,
			ISimpleBlockRenderingHandler handler) {
		instance().renders.put(renderId, handler);
	}

	public boolean renderBlock(int renderId, IBlockState state, BlockPos pos,
			IBlockAccess world, WorldRenderer renderer) {
		if (!renders.containsKey(renderId))
			return false;
		return renders.get(renderId).renderWorldBlock(world, pos, state,
				renderId, renderer);
	}

	public void renderBlockBrightness(int renderId, IBlockState state,
			float brightness) {
		if (!renders.containsKey(renderId))
			return;
		renders.get(renderId)
				.renderBlockBrightness(renderId, state, brightness);
	}

	public void renderInventoryBlock(ItemStack stack,
			TransformType transformType) {
		int renderId = ((ItemBlock) stack.getItem()).getBlock().getRenderType();
		if (!renders.containsKey(renderId))
			return;
		renders.get(renderId).renderInventoryBlock(stack, transformType,
				renderId);
	}

	public boolean shouldRender3DInInventory(ItemStack stack) {
		System.out.println("shouldRender3DInInventory");
		int renderId = ((ItemBlock) stack.getItem()).getBlock().getRenderType();
		if (!renders.containsKey(renderId))
			return false;
		return renders.get(renderId).shouldRender3DInInventory(renderId);
	}

	public void injectTexture(TextureMap map) {
		loader.setTextureMap(map);

		for (ISimpleBlockRenderingHandler isbrh : renders.values())
			isbrh.loadTextures(loader);
	}

	public static void renderItemBody(RenderItem renderItem, ItemStack stack,
			IBakedModel model, TransformType transformType) {
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
			instance().renderInventoryBlock(stack, transformType);
		} else {
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			renderItem.renderModel(model, stack);

			if (stack.hasEffect()) {
				renderItem.renderEffect(model);
			}
		}

		GlStateManager.popMatrix();
	}

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
