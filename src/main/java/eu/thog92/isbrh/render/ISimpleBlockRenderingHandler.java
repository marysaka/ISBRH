package eu.thog92.isbrh.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public interface ISimpleBlockRenderingHandler {
	
	public abstract void renderInventoryBlock(ItemStack itemStack, int renderId);

	public abstract boolean renderWorldBlock(IBlockAccess world, BlockPos pos, IBlockState state, int renderId, WorldRenderer renderer);

	public abstract void renderBlockBrightness(int renderId, IBlockState state, float brightness);

	public abstract void loadTextures(TextureLoader loader);

	public abstract boolean shouldRender3DInInventory(int renderId);

	public abstract int getRenderId();

	public abstract TextureAtlasSprite getSidedTexture(EnumFacing facing);
}
