package eu.thog92.isbrh.test;

import eu.thog92.isbrh.ISBRH;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTest extends Block {

    public BlockTest() {
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
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos,
                                        EnumFacing face) {
        return true;
    }

}
