package eu.thog92.isbrh.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public interface ISimpleBlockRenderingHandler {
	
	void renderInventoryBlock(ItemStack itemStack, int renderId);

	boolean renderWorldBlock(IBlockAccess world, BlockPos pos, IBlockState state, int renderId, WorldRenderer renderer);

	void renderBlockBrightness(int renderId, IBlockState state, float brightness);

	void loadTextures(TextureLoader loader);

	boolean shouldRender3DInInventory(int renderId);

	int getRenderId();

	TextureAtlasSprite getSidedTexture(EnumFacing facing);
}
