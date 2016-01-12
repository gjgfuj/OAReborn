package tk.sandradev.oareborn.internal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Sandra on 11/01/2016.
 */
public class BlockTest extends Block {
	public BlockTest() {
		super(Material.rock);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float ax,float ay,float az) {
		player.addChatComponentMessage(new ChatComponentText("Hi ".concat(player.getDisplayNameString())));
		return super.onBlockActivated(world, pos,state,player,side,ax,ay,az);
	}
}
