package tk.sandradev.oareborn;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tk.sandradev.oareborn.internal.CommonProxy;
import tk.sandradev.oareborn.lasers.OARebornLasers;

/**
 * Created by Sandra on 11/01/2016.
 */
@Mod(modid = "OAReborn")
public class OAReborn {
    public static Item creativeRFFiller = null;
    public static BlockEnergyReader energyReader = null;
    public static CreativeTabs tab = new CreativeTabs("oareborn") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(OARebornLasers.laserMirror);
        }
    };
    @SidedProxy(serverSide = "tk.sandradev.oareborn.internal.CommonProxy", clientSide = "tk.sandradev.oareborn.internal.ClientProxy")
    public static CommonProxy proxy;
    public static Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();
        creativeRFFiller = new ItemCreativeRF();
        proxy.registerItem(creativeRFFiller, "oareborn:creativeRFFiller").setCreativeTab(tab);
        energyReader = new BlockEnergyReader();
        proxy.registerBlock(energyReader, "oareborn:energyReader",BlockEnergyReader.TE.class).setCreativeTab(tab);

    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        config.save();
    }
}
