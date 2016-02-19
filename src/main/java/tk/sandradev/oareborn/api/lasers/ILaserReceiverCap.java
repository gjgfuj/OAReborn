package tk.sandradev.oareborn.api.lasers;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLLog;

import java.util.concurrent.Callable;

public interface ILaserReceiverCap {
    Object receiveLaser(ILaser laser);

    boolean canReceiveLaser(ILaser laser);

    class Storage implements Capability.IStorage<ILaserReceiverCap> {
        @Override
        public NBTBase writeNBT(Capability<ILaserReceiverCap> capability, ILaserReceiverCap instance, EnumFacing side) {
            return null;
        }

        public void readNBT(Capability<ILaserReceiverCap> capability, ILaserReceiverCap instance, EnumFacing side, NBTBase nbt) {
        }
    }

    class Factory implements Callable<ILaserReceiverCap> {
        @Override
        public ILaserReceiverCap call() throws Exception {
            return new Default();
        }

        static class Default implements ILaserReceiverCap {

            @Override
            public Object receiveLaser(ILaser laser) {
                FMLLog.warning("[OALasers] Default implementation of laser used! PROBLEMS HERE.");
                return null;
            }

            @Override
            public boolean canReceiveLaser(ILaser laser) {
                return true;
            }
        }
    }
}
