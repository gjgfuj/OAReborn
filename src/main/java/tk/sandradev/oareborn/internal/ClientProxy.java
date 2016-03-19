package tk.sandradev.oareborn.internal;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by Sandra on 12/01/2016.
 */
public class ClientProxy extends CommonProxy {

    public Block registerBlock(Block block, String name) {
        block = super.registerBlock(block, name);

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(name, "inventory"));
        return block;
    }

    public Item registerItem(Item item, String name) {
        item = super.registerItem(item, name);

        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
        return item;
    }
}
