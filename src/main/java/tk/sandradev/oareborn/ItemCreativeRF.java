package tk.sandradev.oareborn;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Sandra on 12/01/2016.
 */
public class ItemCreativeRF extends Item {
	@Override
	public boolean onItemUse(ItemStack stack_,
	                         EntityPlayer player,
	                         World world,
	                         BlockPos pos,
	                         EnumFacing face,
	                         float ix,
	                         float iy,
	                         float iz) {
		TileEntity te = world.getTileEntity(pos);
		if (world.getTileEntity(pos) instanceof IEnergyReceiver)
		{
			IEnergyReceiver r = (IEnergyReceiver) te;
			r.receiveEnergy(face,r.getMaxEnergyStored(face),false);
			return true;
		}
		return false;
	}
}
