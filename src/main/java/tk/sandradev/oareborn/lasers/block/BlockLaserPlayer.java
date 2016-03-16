package tk.sandradev.oareborn.lasers.block;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.LaserUtil;
import tk.sandradev.oareborn.api.lasers.types.LaserPlayer;
import tk.sandradev.oareborn.internal.OABlock;

/**
 * Created by Sandra on 12/01/2016.
 */
public class BlockLaserPlayer extends BlockLaserSender implements ITileEntityProvider {
    public static PropertyBool POWERED = PropertyBool.create("powered");

    public BlockLaserPlayer() {
        setDefaultState(this.blockState.getBaseState());
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, POWERED,SINGLESIDE,FACING);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        return super.getActualState(state,access,pos).withProperty(POWERED, ((TE) access.getTileEntity(pos)).getEnergyStored(null) >= 3000);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side) {
        if (super.onBlockActivated(world,pos,state,player,side)) return true;
        world.markBlockForUpdate(pos);
        if (!world.isRemote) {
            ILaser laser = new LaserPlayer(player);
            TE te = ((TE) world.getTileEntity(pos));
            if (te.getEnergyStored(null) >= 3000)
                if (LaserUtil.canSendLaser(world, pos, getSide(world,pos,state,side), laser)) {
                    te.storage.extractEnergy(3000, false);
                    player.addChatComponentMessage(new ChatComponentText("WHOOSH"));
                    LaserUtil.sendLaser(world, pos, getSide(world,pos,state,side), laser);
                } else {
                    player.addChatComponentMessage(new ChatComponentText("But nothing happened."));
                }
            else {
                player.addChatComponentMessage(new ChatComponentText("It spluttered to a halt."));
                world.markBlockForUpdate(pos);
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TE();
    }

    public static class TE extends TileEntity implements IEnergyReceiver {
        //TODO: ADD CAPABILITY SUPPORT
        EnergyStorage storage = new EnergyStorage(32000, 3000);

        @Override
        public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
            if (getEnergyStored(from) + maxReceive >= 3000 && getEnergyStored(from) < 3000) {
                worldObj.markBlockForUpdate(pos);
            }
            return storage.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public Packet getDescriptionPacket() {
            S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(pos, getBlockMetadata(), new NBTTagCompound());
            packet.getNbtCompound().setInteger("energy", storage.getEnergyStored());
            return packet;
        }

        @Override
        public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
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
