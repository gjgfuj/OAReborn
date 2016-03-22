package tk.sandradev.oareborn.integration;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tk.sandradev.oareborn.OAReborn;
import tk.sandradev.oareborn.integration.block.BlockLaserData;
import tk.sandradev.oareborn.internal.CommonProxy;

@Mod(modid="OAReborn|Integration",name="OAReborn Integration")
public class OARebornIntegration {
    public static BlockLaserData laserData = null;
    public CommonProxy proxy;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy = OAReborn.proxy;
        CreativeTabs tab = OAReborn.tab;
        if (Loader.isModLoaded("OpenComputers"))
        {
            if (Loader.isModLoaded("OAReborn|Lasers")) {
                laserData = new BlockLaserData();
                proxy.registerBlock(laserData, "oalasers:laserData",BlockLaserData.TE.class).setCreativeTab(tab);
            }
        }
    }
}
