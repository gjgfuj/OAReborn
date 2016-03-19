package tk.sandradev.oareborn.lasers.block;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.LaserUtil;
import tk.sandradev.oareborn.api.lasers.types.LaserPlayer;
import tk.sandradev.oareborn.internal.BlockPointerOrSided;
import tk.sandradev.oareborn.lasers.OARebornLasers;

/**
 * Created by Sandra on 12/01/2016.
 */
public class BlockLaserPlayer extends BlockPointerOrSided implements ITileEntityProvider {
    public static void causeBlockUpdate(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos,state,state,3);
    }
    public static PropertyBool POWERED = PropertyBool.create("powered");

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    public String type() {
        return "LaserPlayer";
    }

    public BlockLaserPlayer() {
        setDefaultState(this.blockState.getBaseState());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED,SINGLESIDE,FACING);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        return super.getActualState(state,access,pos).withProperty(POWERED, ((TE) access.getTileEntity(pos)).getEnergyStored(null) >= 3000);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        if (super.onBlockActivated(world,pos,state,player,side)) return true;
        causeBlockUpdate(world,pos);
        if (!world.isRemote) {
            ILaser laser = new LaserPlayer(player);
            TE te = ((TE) world.getTileEntity(pos));
            if (te.getEnergyStored(null) >= 3000)
                if (LaserUtil.canSendLaser(world, pos, getSide(world,pos,state,side), laser)) {
                    te.storage.extractEnergy(3000, false);
                    player.addChatComponentMessage(new TextComponentString("WHOOSH"));
                    LaserUtil.sendLaser(world, pos, getSide(world,pos,state,side), laser);
                } else {
                    player.addChatComponentMessage(new TextComponentString("But nothing happened."));
                }
            else {
                player.addChatComponentMessage(new TextComponentString("It spluttered to a halt."));
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TE();
    }

    public static class TE extends TileEntity implements IEnergyReceiver {
        BlockLaserPlayer block = OARebornLasers.laserPlayer;
        //TODO: ADD CAPABILITY SUPPORT
        EnergyStorage storage = new EnergyStorage(32000, 6000);

        @Override
        public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
            if (storage.getEnergyStored()<3000) causeBlockUpdate(worldObj,pos);
            return storage.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public Packet getDescriptionPacket() {
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, getBlockMetadata(), new NBTTagCompound());
            packet.getNbtCompound().setInteger("energy", storage.getEnergyStored());
            return packet;
        }

        @Override
        public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
            storage.setEnergyStored(packet.getNbtCompound().getInteger("energy"));
        }

        @Override
        public int getEnergyStored(EnumFacing from) {
            return storage.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored(EnumFacing from) {
            return storage.getMaxEnergyStored();
        }

        @Override
        public boolean canConnectEnergy(EnumFacing from) {
            return true;
        }

        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            storage.readFromNBT(tag);
        }

        public void writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            storage.writeToNBT(tag);
        }
        public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
        {
            return oldState.getBlock() != newState.getBlock();
        }
    }
}
