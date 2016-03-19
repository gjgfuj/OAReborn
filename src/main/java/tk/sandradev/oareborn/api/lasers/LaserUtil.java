package tk.sandradev.oareborn.api.lasers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class LaserUtil {
    @CapabilityInject(ILaserReceiverCap.class)
    public static Capability<ILaserReceiverCap> cap = null;

    private static void spawnParticles(WorldServer world, int length, BlockPos opos, BlockPos pos, EnumFacing face) {
        world.spawnParticle(EnumParticleTypes.REDSTONE, (opos.getX() + pos.getX()) / 2 + 0.5f, (opos.getY() + pos.getY()) / 2 + 0.5f, (opos.getZ() + pos.getZ()) / 2 + 0.5f, length * 2, face.getFrontOffsetX() * (length - 1) / 2, face.getFrontOffsetY() * (length - 1) / 2, face.getFrontOffsetZ() * (length - 1) / 2, 0f);
    }
    public static BlockPos finalPos(World world, BlockPos opos, EnumFacing face) {
        IBlockState b;
        BlockPos pos = opos;
        int i = 0;
        do {
            i++;
            pos = pos.offset(face);
            b = world.getBlockState(pos);
            TileEntity t = world.getTileEntity(pos);
            if (t != null && t.hasCapability(cap, face.getOpposite())) {
                spawnParticles((WorldServer) world, i, opos, pos, face);
                return pos;
            }
            if (b.getBlock() instanceof ILaserReceiver) {
                //OARebornLasers.network.sendToDimension(new LaserMessage(opos,pos,face),world.provider.getDimensionId());
                spawnParticles((WorldServer) world, i, opos, pos, face);
                return pos;
            }
        }
        while (i < 300 && !(b.getBlock().isSideSolid(world.getBlockState(pos),world, pos, face) && b.getBlock().isSideSolid(world.getBlockState(pos),world, pos, face.getOpposite())));
        return pos;
    }

    public static Object sendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser) {
        BlockPos finalPos = finalPos(world, pos, face);
        IBlockState b = world.getBlockState(finalPos);
        TileEntity t = world.getTileEntity(pos);
        if (t != null && t.hasCapability(cap, face.getOpposite())) {
            laser.transitCallback(pos,finalPos);
            return t.getCapability(cap, face.getOpposite()).receiveLaser(laser);
        } else if (b.getBlock() instanceof ILaserReceiver) {
            laser.transitCallback(pos,finalPos);
            return ((ILaserReceiver) b.getBlock()).receiveLaser(world, finalPos, b, face.getOpposite(), laser);
        }
        return null;
    }

    public static boolean canSendLaser(World world, BlockPos pos, EnumFacing face, ILaser laser) {
        BlockPos finalPos = finalPos(world, pos, face);
        IBlockState b = world.getBlockState(finalPos);
        TileEntity t = world.getTileEntity(pos);
        if (t != null && t.hasCapability(cap, face.getOpposite())) {
            return t.getCapability(cap, face.getOpposite()).canReceiveLaser(laser);
        } else if (b.getBlock() instanceof ILaserReceiver) {
            return ((ILaserReceiver) b.getBlock()).canReceiveLaser(world, finalPos, b, face.getOpposite(), laser);
        }
        return false;
    }
}
