package eu.thog92.isbrh.test;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import eu.thog92.isbrh.ISBRH;
import eu.thog92.isbrh.render.ISimpleBlockRenderingHandler;
import eu.thog92.isbrh.render.TextureLoader;

public class RenderTest implements ISimpleBlockRenderingHandler {

	public final ResourceLocation textureLocation = new ResourceLocation("isbrhcore:blocks/test");
	
	@Override
	public void renderInventoryBlock(ItemStack itemStack, int renderId) {
		
		ItemStack demoStack = new ItemStack(Blocks.sand, 1);
		Minecraft.getMinecraft().getRenderItem().renderItem(demoStack, Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(demoStack));
		
	}
	

	
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, BlockPos pos,
			IBlockState state, int id, WorldRenderer renderer) {
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(textureLocation.toString());
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		
		renderer.addVertexWithUV(x, y, z, sprite.getMaxU(), sprite.getMinV());
		renderer.addVertexWithUV(x, y + 1, z, sprite.getMaxU(), sprite.getMaxV());
		renderer.addVertexWithUV(x + 1, y + 1, z, sprite.getMinU(), sprite.getMaxV());
		renderer.addVertexWithUV(x + 1, y, z, sprite.getMinU(), sprite.getMinV());
		
		renderer.addVertexWithUV(x + 1, y, z, sprite.getMaxU(), sprite.getMinV());
		renderer.addVertexWithUV(x + 1, y + 1, z, sprite.getMaxU(), sprite.getMaxV());
		renderer.addVertexWithUV(x + 1, y + 1, z + 1, sprite.getMinU(), sprite.getMaxV());
		renderer.addVertexWithUV(x + 1, y, z + 1, sprite.getMinU(), sprite.getMinV());

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int id) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ISBRH.testId;
	}

	@Override
	public void renderBlockBrightness(int renderId, IBlockState state,
			float brightness) {
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.sand.getDefaultState(), brightness);
	}

	@Override
	public void loadTextures(TextureLoader loader) {
		loader.registerTexture(textureLocation);
	}

}
