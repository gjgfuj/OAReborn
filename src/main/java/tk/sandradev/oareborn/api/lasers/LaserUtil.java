package tk.sandradev.oareborn.api.lasers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Sandra on 12/01/2016.
 */
public class LaserUtil {
	public static Object sendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser)
	{
		IBlockState b;
		int i = 0;
		do {
			i++;
			pos = pos.offset(face);
			b = world.getBlockState(pos);
			if (b.getBlock() instanceof ILaserReceiver)
				return ((ILaserReceiver) b.getBlock()).receiveLaser(world,pos,b,face.getOpposite(),laser);
		} while (i < 300 && !(b.getBlock().isSideSolid(world,pos,face) && b.getBlock().isSideSolid(world,pos,face.getOpposite())));
		return false;
	}
	public static boolean canSendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser)
	{
		IBlockState b;
		int i = 0;
		do {
			i++;
			pos = pos.offset(face);
			b = world.getBlockState(pos);
			if (b.getBlock() instanceof ILaserReceiver)
				return ((ILaserReceiver) b.getBlock()).canReceiveLaser(world,pos,b,face.getOpposite(),laser);
		} while (i<300 && !(b.getBlock().isSideSolid(world,pos,face) && b.getBlock().isSideSolid(world,pos,face.getOpposite())));
		System.out.println("Laser stopped on Block: ".concat(b.toString()));
		return false;
	}
}
