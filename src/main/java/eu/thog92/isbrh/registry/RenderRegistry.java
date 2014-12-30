package eu.thog92.isbrh.registry;

import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
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
		return renders.get(renderId).renderWorldBlock(world, pos, state, renderId, renderer);
	}

	public void renderBlockBrightness(int renderId, IBlockState state, float brightness) {
		if (!renders.containsKey(renderId))
			return;
		renders.get(renderId).renderBlockBrightness(renderId, state, brightness);
	}

	public void renderInventoryBlock(ItemStack stack) {
		int renderId = ((ItemBlock)stack.getItem()).getBlock().getRenderType();
		if (!renders.containsKey(renderId))
			return;
		renders.get(renderId).renderInventoryBlock(stack, renderId);
	}

	public boolean shouldRender3DInInventory(ItemStack stack) {
		int renderId = ((ItemBlock)stack.getItem()).getBlock().getRenderType();
		if (!renders.containsKey(renderId))
			return false;
		return renders.get(renderId).shouldRender3DInInventory(renderId);
	}

	public void injectTexture(TextureMap map) {
		loader.setTextureMap(map);

		for(ISimpleBlockRenderingHandler isbrh : renders.values())
			isbrh.loadTextures(loader);
	}
}
