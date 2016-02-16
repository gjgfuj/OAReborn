package tk.sandradev.oareborn.lasers;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tk.sandradev.oareborn.OAReborn;
import tk.sandradev.oareborn.internal.CommonProxy;
import tk.sandradev.oareborn.lasers.block.BlockLaserMirror;
import tk.sandradev.oareborn.lasers.block.BlockLaserPlayer;
import tk.sandradev.oareborn.lasers.block.BlockLaserReceiver;

/**
 * Created by Sandra on 12/01/2016.
 */
@Mod(modid = "OALasers")
public class OARebornLasers {
    public static Block laserReceiver = null;
    public static Block laserMirror = null;
    public static Block laserPlayer = null;
    public static CommonProxy proxy = null;
    public static SimpleNetworkWrapper network = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy = OAReborn.proxy;
        CreativeTabs tab = OAReborn.tab;
        laserReceiver = new BlockLaserReceiver();
        proxy.registerBlock(laserReceiver, "oareborn:laserReceiver").setCreativeTab(tab);
        laserMirror = new BlockLaserMirror();
        proxy.registerBlock(laserMirror, "oareborn:laserMirror").setCreativeTab(tab);
        laserPlayer = new BlockLaserPlayer();
        proxy.registerBlock(laserPlayer, "oareborn:laserPlayer", BlockLaserPlayer.TE.class).setCreativeTab(tab);
        network = NetworkRegistry.INSTANCE.newSimpleChannel("oalasers");
        network.registerMessage(LaserMessage.class, LaserMessage.class, 0, Side.SERVER);
    }
}
