package tk.sandradev.oareborn.api.lasers;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import tk.sandradev.oareborn.lasers.LaserMessage;
import tk.sandradev.oareborn.lasers.OARebornLasers;

/**
 * Created by Sandra on 12/01/2016.
 */
public class LaserUtil {
	public static BlockPos finalPos(World world, BlockPos pos, EnumFacing face) {
		IBlockState b;
		int i = 0;
		do {
			i++;
			pos = pos.offset(face);
			b = world.getBlockState(pos);
			if (b.getBlock() instanceof ILaserReceiver)
				return pos;
		} while (i < 300 && !(b.getBlock().isSideSolid(world,pos,face) && b.getBlock().isSideSolid(world,pos,face.getOpposite())));
		return pos;
	}
	public static Object sendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser)
	{
		BlockPos finalPos = finalPos(world,pos,face);
		IBlockState b = world.getBlockState(finalPos);
		if (!world.isRemote)
			OARebornLasers.network.sendToDimension(new LaserMessage(pos,finalPos,face),world.provider.getDimensionId());
		return ((ILaserReceiver) b.getBlock()).receiveLaser(world,pos,b,face.getOpposite(),laser);
	}
	public static boolean canSendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser)
	{
		IBlockState b = world.getBlockState(finalPos(world,pos,face));
		return b.getBlock() instanceof ILaserReceiver && ((ILaserReceiver) b.getBlock()).canReceiveLaser(world,pos,b,face.getOpposite(),laser);
	}
}
