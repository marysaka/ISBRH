package eu.thog92.isbrh;

import eu.thog92.isbrh.registry.RenderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ISBRHDispatcher extends BlockRendererDispatcher
{
    private final BlockRendererDispatcher oldDispatcher;

    public ISBRHDispatcher(BlockRendererDispatcher oldDispatcher, BlockModelShapes modelShapes, GameSettings settings)
    {
        super(modelShapes, settings);
        this.oldDispatcher = oldDispatcher;
    }
    
    
    @Override
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, WorldRenderer worldRendererIn)
    {
        final boolean result = oldDispatcher.renderBlock(state, pos, blockAccess, worldRendererIn);
        if(!result && state.getBlock().getRenderType() > 4)
        {
            return RenderRegistry.instance().renderBlock(state.getBlock().getRenderType(), state, pos, blockAccess, worldRendererIn);
        }
        
        return result;
    }
    
    
    @Override
    public void renderBlockBrightness(IBlockState state, float brightness)
    {
        oldDispatcher.renderBlockBrightness(state, brightness);
        if(state.getBlock().getRenderType() > 4)
            RenderRegistry.instance().renderBlockBrightness(state.getBlock().getRenderType(), state, brightness);
    }
    
    @Override
    public void renderBlockDamage(IBlockState state, BlockPos pos, TextureAtlasSprite texture, IBlockAccess blockAccess)
    {
        oldDispatcher.renderBlockDamage(state, pos, texture, blockAccess);
        if(state.getBlock().getRenderType() > 4)
            RenderRegistry.instance().renderBlock(state.getBlock().getRenderType(), state, pos, blockAccess, Tessellator.getInstance().getWorldRenderer());
    }

    @Override
    public BlockModelRenderer getBlockModelRenderer()
    {
        return oldDispatcher.getBlockModelRenderer();
    }

    @Override
    public IBakedModel getModelFromBlockState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return oldDispatcher.getModelFromBlockState(state, worldIn, pos);
    }

    @Override
    public boolean isRenderTypeChest(Block block, int meta)
    {
        return oldDispatcher.isRenderTypeChest(block, meta);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        oldDispatcher.onResourceManagerReload(resourceManager);
    }
}
