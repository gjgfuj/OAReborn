package tk.sandradev.oareborn.integration.block;

import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.TileEntityEnvironment;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import tk.sandradev.oareborn.api.integration.oc.lasers.LaserData;
import tk.sandradev.oareborn.api.lasers.ILaser;
import tk.sandradev.oareborn.api.lasers.ILaserReceiverCap;
import tk.sandradev.oareborn.api.lasers.LaserUtil;
import tk.sandradev.oareborn.integration.OARebornIntegration;
import tk.sandradev.oareborn.internal.BlockPointerOrSided;

public class BlockLaserData extends BlockPointerOrSided implements ITileEntityProvider {
    @Override
    public boolean enabledByDefault() {
        return true;
    }
    @Override
    public boolean switchable()
    {
        return false;
    }

    @Override
    public String type() {
        return "laserData";
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TE();
    }
    public static class TE extends TileEntityEnvironment {
        public class LaserReceiverCap implements ILaserReceiverCap {

            @Override
            public Object receiveLaser(ILaser laser) {
                LaserData data = ((LaserData) laser);
                Object[] newdata = new Object[data.data.length+1];
                newdata[0] = "laser.message";
                System.arraycopy(data.data,0,newdata,1,data.data.length);
                System.out.println(newdata[1]);
                if (data.address != null)
                    node.sendToAddress(data.address,"computer.signal",newdata);
                else
                    node.sendToReachable("computer.signal",newdata);
                return true;
            }

            @Override
            public boolean canReceiveLaser(ILaser laser) {
                return laser instanceof LaserData;
            }
        }
        @CapabilityInject(ILaserReceiverCap.class)
        public static Capability<ILaserReceiverCap> laserReceiverCap = null;
        public LaserReceiverCap capInstance = new LaserReceiverCap();
        public TE() {
            node = Network.newNode(this, Visibility.Network).withComponent("dataLaser").withConnector(100).create();
        }
        public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
            return cap == laserReceiverCap || super.hasCapability(cap, facing);
        }
        public <T> T getCapability(Capability<T> cap, EnumFacing facing)
        {
            if (cap == laserReceiverCap) return (T) capInstance;
            return super.getCapability(cap,facing);
        }
        public Object[] send(ILaser laser)
        {
            EnumFacing side = OARebornIntegration.laserData.getSide(worldObj,pos,worldObj.getBlockState(pos),null);
            if (LaserUtil.canSendLaser(worldObj,pos,side,laser))
            {
                LaserUtil.sendLaser(worldObj,pos,side,laser);
                return new Object[] {true};
            }
            return new Object[] {false};

        }
        @Callback
        public Object[] broadcast(Context context,Arguments arguments)
        {
            return send(new LaserData(arguments.toArray()));
        }
        @Callback
        public Object[] sendTo(Context context, Arguments arguments)
        {
            String address = arguments.checkString(0);
            Object[] args = arguments.toArray();
            Object[] data = new Object[args.length-1];
            System.arraycopy(args,1,data,0,args.length-1);
            return send(new LaserData(address,data));
        }
    }
}
