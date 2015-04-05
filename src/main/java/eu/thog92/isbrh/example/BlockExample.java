package eu.thog92.isbrh.example;

import eu.thog92.isbrh.ISBRH;
import eu.thog92.isbrh.render.ITextureHandler;
import eu.thog92.isbrh.render.TextureLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockExample extends Block implements ITextureHandler
{

    @SideOnly(Side.CLIENT)
    private TextureLoader textureLoader;
    
    @SideOnly(Side.CLIENT)
    private final ResourceLocation textureLocation = new ResourceLocation(
            "isbrhcore:blocks/test");

    public BlockExample() {
        super(Material.iron);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return ISBRH.testId;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.SOLID;
    }

    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getSidedTexture(IBlockState state, EnumFacing facing)
    {
        return textureLoader.getTextureMap().getAtlasSprite(textureLocation.toString());
    }

    @Override
    public void loadTextures(TextureLoader loader)
    {
        this.textureLoader = loader;
        loader.registerTexture(textureLocation);
    }

}
