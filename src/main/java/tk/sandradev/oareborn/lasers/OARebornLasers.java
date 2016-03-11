package tk.sandradev.oareborn.lasers;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tk.sandradev.oareborn.OAReborn;
import tk.sandradev.oareborn.api.lasers.ILaserReceiverCap;
import tk.sandradev.oareborn.internal.CommonProxy;
import tk.sandradev.oareborn.lasers.block.BlockLaserMirror;
import tk.sandradev.oareborn.lasers.block.BlockLaserPlayer;
import tk.sandradev.oareborn.lasers.block.BlockLaserReceiver;
import tk.sandradev.oareborn.lasers.block.BlockLaserSplitter;

@Mod(modid = "OALasers")
public class OARebornLasers {
    public static Block laserReceiver = null;
    public static Block laserMirror = null;
    public static Block laserPlayer = null;
    public static Block laserSplitter = null;
    public static Item itemLaserLens = null;
    public static Item itemLaserProjector = null;
    public static Item itemLaserMirror = null;
    public static CommonProxy proxy = null;
    //public static SimpleNetworkWrapper network = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy = OAReborn.proxy;
        CreativeTabs tab = OAReborn.tab;
        CapabilityManager.INSTANCE.register(ILaserReceiverCap.class, new ILaserReceiverCap.Storage(), new ILaserReceiverCap.Factory());
        laserReceiver = new BlockLaserReceiver();
        proxy.registerBlock(laserReceiver, "oareborn:laserReceiver").setCreativeTab(tab);
        laserMirror = new BlockLaserMirror();
        proxy.registerBlock(laserMirror, "oareborn:laserMirror").setCreativeTab(tab);
        laserSplitter = new BlockLaserSplitter();
        proxy.registerBlock(laserSplitter, "oareborn:laserSplitter").setCreativeTab(tab);
        laserPlayer = new BlockLaserPlayer();
        proxy.registerBlock(laserPlayer, "oareborn:laserPlayer", BlockLaserPlayer.TE.class).setCreativeTab(tab);
        itemLaserLens = new Item();
        proxy.registerItem(itemLaserLens, "oareborn:itemLaserLens").setCreativeTab(tab);
        itemLaserMirror = new Item();
        proxy.registerItem(itemLaserMirror, "oareborn:itemLaserMirror").setCreativeTab(tab);
        itemLaserProjector = new Item();
        proxy.registerItem(itemLaserProjector, "oareborn:itemLaserProjector").setCreativeTab(tab);
        //network = NetworkRegistry.INSTANCE.newSimpleChannel("oalasers");
        //network.registerMessage(LaserMessage.class, LaserMessage.class, 0, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        GameRegistry.addRecipe(new ShapedOreRecipe(itemLaserLens, "mgm", "s#s", "mrm", 'm', "ingotIron", 's', "ingotGold", '#', "blockGlass", 'r', "dustRedstone", 'g', "dustGlowstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemLaserMirror, "mgl", "ggg", "lgm", 'l', itemLaserLens, 'm', "ingotIron", 'g', "blockGlass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemLaserProjector, "l g", " gl", "g g", 'g', "dustGlowstone", 'l', itemLaserLens));
        GameRegistry.addRecipe(new ShapedOreRecipe(laserReceiver, "mpm", "lgm", "mgm", 'm', "ingotIron", 'p', itemLaserProjector, 'l', itemLaserLens));
        GameRegistry.addRecipe(new ShapedOreRecipe(laserMirror, " lm", "lil", "ml ", 'm', "ingotIron", 'i', itemLaserMirror, 'l', itemLaserLens));
        GameRegistry.addRecipe(new ShapedOreRecipe(laserPlayer, "mem", "elp", "mrm", 'm', "ingotIron", 'e', Items.ender_pearl, 'l', itemLaserLens, 'p', itemLaserProjector, 'r', "blockRedstone"));
    }
}
