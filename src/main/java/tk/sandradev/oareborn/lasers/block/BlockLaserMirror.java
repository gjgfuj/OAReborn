package tk.sandradev.oareborn.lasers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.ILaserReceiver;
import tk.sandradev.oareborn.api.lasers.LaserUtil;
import tk.sandradev.oareborn.internal.OABlock;

/**
 * Created by Sandra on 12/01/2016.
 */
public class BlockLaserMirror extends OABlock implements ILaserReceiver {
    public static final PropertyEnum<Diagonals> DIR = PropertyEnum.create("dir", Diagonals.class);

    public BlockLaserMirror() {
        super(Material.iron);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{DIR});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DIR, Diagonals.values()[meta]);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(DIR).ordinal();
    }

    protected EnumFacing getSendFace(IBlockState state, EnumFacing face) {
        final Diagonals dir = state.getValue(DIR);
        EnumFacing pos1 = dir.s1;
        EnumFacing pos2 = dir.s2;
        if (face == pos1) return pos2;
        else if (face == pos2) return pos1;
        else if (face == pos1.getOpposite()) return pos2.getOpposite();
        else if (face == pos2.getOpposite()) return pos1.getOpposite();
        else return face.getOpposite();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Override
    public Object receiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser) {
        return LaserUtil.sendLaser(world, pos, getSendFace(state, face), laser);
    }

    @Override
    public boolean canReceiveLaser(World world, BlockPos pos, IBlockState state, EnumFacing face, ILaser laser) {
        return LaserUtil.canSendLaser(world, pos, getSendFace(state, face), laser);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        if (!world.isRemote) {
            world.setBlockState(pos, state.cycleProperty(DIR));
        }
        return true;
    }

    public enum Diagonals implements IStringSerializable {
        NE_SW("NE/SW", EnumFacing.NORTH, EnumFacing.EAST), NW_SE("NW/SE", EnumFacing.NORTH, EnumFacing.WEST), UN_DS("UN/DS", EnumFacing.UP, EnumFacing.NORTH), US_DN("US/DN", EnumFacing.UP, EnumFacing.SOUTH), UE_DW("UE/DW", EnumFacing.UP, EnumFacing.EAST), UW_DE("UW/DE", EnumFacing.UP, EnumFacing.WEST);
        String n;
        EnumFacing s1;
        EnumFacing s2;

        Diagonals(String name, EnumFacing s1, EnumFacing s2) {
            n = name;
            this.s1 = s1;
            this.s2 = s2;
        }

        public static Diagonals fromDirs(EnumFacing dir1, EnumFacing dir2) {
            for (Diagonals dia : values()) {
                if (dir1 == dia.s1 && dir2 == dia.s2 || dir1.getOpposite() == dia.s1 && dir2.getOpposite() == dia.s2)
                    return dia;
            }
            return NE_SW;
        }

        @Override
        public String toString() {
            return n;
        }

        @Override
        public String getName() {
            return n;
        }
    }
}
