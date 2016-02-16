package tk.sandradev.oareborn.api.lasers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LaserUtil {
    public static BlockPos finalPos(World world, BlockPos opos, EnumFacing face) {
        IBlockState b;
        BlockPos pos = opos;
        int i = 0;
        do {
            i++;
            pos = pos.offset(face);
            b = world.getBlockState(pos);
            if (b.getBlock() instanceof ILaserReceiver) {
                //OARebornLasers.network.sendToDimension(new LaserMessage(opos,pos,face),world.provider.getDimensionId());
                ((WorldServer) world).spawnParticle(EnumParticleTypes.REDSTONE, (opos.getX() + pos.getX()) / 2 + 0.5f, (opos.getY() + pos.getY()) / 2 + 0.5f, (opos.getZ() + pos.getZ()) / 2 + 0.5f, i * 3, face.getFrontOffsetX() * i / 2, face.getFrontOffsetY() * i / 2, face.getFrontOffsetZ() * i / 2, 0f);
                return pos;
            }
        }
        while (i < 300 && !(b.getBlock().isSideSolid(world, pos, face) && b.getBlock().isSideSolid(world, pos, face.getOpposite())));
        return pos;
    }

    public static Object sendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser) {
        BlockPos finalPos = finalPos(world, pos, face);
        IBlockState b = world.getBlockState(finalPos);
        return ((ILaserReceiver) b.getBlock()).receiveLaser(world, finalPos, b, face.getOpposite(), laser);
    }

    public static boolean canSendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser) {
        BlockPos finalPos = finalPos(world, pos, face);
        IBlockState b = world.getBlockState(finalPos);
        return b.getBlock() instanceof ILaserReceiver && ((ILaserReceiver) b.getBlock()).canReceiveLaser(world, finalPos, b, face.getOpposite(), laser);
    }
}
