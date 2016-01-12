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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import tk.sandradev.oareborn.internal.OABlock;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.LaserUtil;
import tk.sandradev.oareborn.api.lasers.types.LaserPlayer;

/**
 * Created by Sandra on 12/01/2016.
 */
public class BlockLaserPlayer extends OABlock implements ITileEntityProvider {
	public static class TE extends TileEntity implements IEnergyReceiver {
		//TODO: ADD CAPABILITY SUPPORT
		EnergyStorage storage = new EnergyStorage(32000, 3000);
		@Override
		public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
			if (getEnergyStored(from)+maxReceive >= 3000 && getEnergyStored(from) < 3000)
			{
				worldObj.markBlockForUpdate(pos);
			}
			return storage.receiveEnergy(maxReceive, simulate);
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
		public void readFromNBT(NBTTagCompound tag)
		{
			super.readFromNBT(tag);
			storage.readFromNBT(tag);
		}
		public void writeToNBT(NBTTagCompound tag)
		{
			super.writeToNBT(tag);
			storage.writeToNBT(tag);
		}
	}
	public static PropertyBool POWERED = PropertyBool.create("powered");
	public BlockLaserPlayer() {
		super(Material.iron);
		setDefaultState(this.blockState.getBaseState());
	}
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{POWERED});
	}
	public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
		return state.withProperty(POWERED, ((TE)access.getTileEntity(pos)).getEnergyStored(null) >= 3000);
	}
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side)
	{
		world.markBlockForUpdate(pos);
		if (!world.isRemote) {
			ILaser laser = new LaserPlayer(player);
			TE te = ((TE)world.getTileEntity(pos));
			if (te.getEnergyStored(null)>=3000)
			if (LaserUtil.canSendLaser(world, pos, side.getOpposite(), laser)) {
				te.storage.extractEnergy(3000,false);
				player.addChatComponentMessage(new ChatComponentText("WHOOSH"));
				LaserUtil.sendLaser(world, pos, side.getOpposite(), laser);
			} else
			{
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
}
