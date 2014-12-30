package eu.thog92.isbrh.test;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import eu.thog92.isbrh.ISBRH;

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

}
