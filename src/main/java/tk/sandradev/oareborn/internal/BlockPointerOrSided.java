package tk.sandradev.oareborn.internal;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tk.sandradev.oareborn.OAReborn;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.internal.OABlock;
import tk.sandradev.oareborn.lasers.OARebornLasers;

public abstract class BlockPointerOrSided extends OABlock {
    public static PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing",EnumFacing.class);
    public static PropertyBool SINGLESIDE = PropertyBool.create("singleSide");
    public boolean enabled;
    public abstract boolean enabledByDefault();
    public abstract String type();
    public boolean switchable() {return true;}
    public BlockPointerOrSided()
    {
        enabled = switchable() ? OAReborn.config.getBoolean(type(), "singlesided", enabledByDefault(), type().concat(": true: use a single output, all other sides input method, false: use a input 1 side, output the opposite side model.")) : enabledByDefault();
    }
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING,EnumFacing.values()[meta]);
    }
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        return state.withProperty(SINGLESIDE,enabled);
    }
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING,SINGLESIDE);
    }
    public boolean isOpaqueCube() {
        return enabled;
    }

    public boolean isFullCube() {
        return enabled;
    }

    public EnumFacing getSide(World world, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return enabled ? state.getValue(FACING) : side.getOpposite();
    }
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        {
            if (player.isSneaking()) {
                world.setBlockState(pos, state.cycleProperty(FACING));
                return true;
            }
            return false;
        }}
}
