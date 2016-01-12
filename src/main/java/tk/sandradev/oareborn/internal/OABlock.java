package tk.sandradev.oareborn.internal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Sandra on 12/01/2016.
 */
public abstract class OABlock extends Block {
	public OABlock(Material mat) {
		super(mat);
	}
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side)
	{
		return false;
	}
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float ax, float ay, float az)
	{
		return onBlockActivated(world,pos,state,player,side);
	}
}
