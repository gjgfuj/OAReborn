package tk.sandradev.oareborn.lasers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.*;
import tk.sandradev.oareborn.internal.OABlock;

public class BlockLaserSplitter extends OABlock implements ILaserReceiver {
    public static IProperty AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockLaserSplitter() {
        super(Material.iron);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{AXIS});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, EnumFacing.Axis.values()[meta]);
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing.Axis) state.getValue(AXIS)).ordinal();
    }
    public EnumFacing getDir1(EnumFacing.Axis axis)
    {
        switch (axis)
        {
            case X:
                return EnumFacing.WEST;
            case Y:
                return EnumFacing.DOWN;
            case Z:
                return EnumFacing.NORTH;
            default:
                return null;
        }
    }
    public EnumFacing getDir2(EnumFacing.Axis axis)
    {
        switch (axis)
        {
            case X:
                return EnumFacing.EAST;
            case Y:
                return EnumFacing.UP;
            case Z:
                return EnumFacing.SOUTH;
            default:
                return null;
        }
    }


    public EnumFacing.Axis getAxis(IBlockState state)
    {
        return (EnumFacing.Axis) state.getValue(AXIS);
    }
    @Override
    public Object receiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser) {
        EnumFacing.Axis axis = getAxis(state);
        SplitLaser lasers = laser.split();
        Object r1 = LaserUtil.sendLaser(world, pos, getDir1(axis), lasers.l1);
        Object r2 = LaserUtil.sendLaser(world,pos, getDir2(axis), lasers.l2);
        return new SplitResult(r1,r2);
    }

    @Override
    public boolean canReceiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser) {
        EnumFacing.Axis axis = getAxis(state);
        SplitLaser lasers = laser.split();
        boolean r1 = LaserUtil.canSendLaser(world, pos, getDir1(axis), lasers.l1);
        boolean r2 = LaserUtil.canSendLaser(world,pos, getDir2(axis),lasers.l2);
        return  laser.canMultiDestination() ? r1&&r2 : r1||r2;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        if (!world.isRemote) {
            world.setBlockState(pos, state.cycleProperty(AXIS));
        }
        return true;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

}
