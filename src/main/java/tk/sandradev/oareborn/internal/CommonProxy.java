package tk.sandradev.oareborn.internal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Sandra on 12/01/2016.
 */
public class CommonProxy {

	public Block registerBlock(Block block, String name)
	{
		GameRegistry.registerBlock(block,name);
		block.setUnlocalizedName(name);
		return block;
	}
	public Block registerBlock(Block block, String name, Class<? extends TileEntity> te)
	{
		GameRegistry.registerTileEntity(te,name);
		return registerBlock(block,name);
	}
	public Item registerItem(Item item, String name)
	{
		GameRegistry.registerItem(item,name);
		item.setUnlocalizedName(name);
		return item;
	}
}
