package lib.cofh.api.energy;

import lib.cofh.util.MathHelper;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Reference implementation of {@link IEnergyStorage}. Use/extend this or implement your own.
 * 
 * @author King Lemming
 */
public class EnergyStorage implements IEnergyStorage
{

    protected int energy;

    protected int capacity;

    protected int maxReceive;

    protected int maxExtract;

    public EnergyStorage(final int capacity)
    {

        this(capacity, capacity, capacity);
    }

    public EnergyStorage(final int capacity,
                         final int maxTransfer)
    {

        this(capacity, maxTransfer, maxTransfer);
    }

    public EnergyStorage(final int capacity,
                         final int maxReceive,
                         final int maxExtract)
    {

        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public EnergyStorage readFromNBT(final NBTTagCompound nbt)
    {

        this.energy = nbt.getInteger("Energy");
        return this;
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound nbt)
    {

        nbt.setInteger("Energy", this.energy);
        return nbt;
    }

    public void setCapacity(final int capacity)
    {

        this.capacity = capacity;

        if (this.energy > capacity){
            this.energy = capacity;
        }
    }

    public void setMaxTransfer(final int maxTransfer)
    {

        this.setMaxReceive(maxTransfer);
        this.setMaxExtract(maxTransfer);
    }

    public void setMaxReceive(final int maxReceive)
    {

        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(final int maxExtract)
    {

        this.maxExtract = maxExtract;
    }

    /* IEnergyStorage */
    @Override
    public int receiveEnergy(final int maxReceive, final boolean doReceive)
    {

        final int energyReceived = MathHelper.minI(this.capacity - this.energy, MathHelper.minI(this.maxReceive, maxReceive));

        if (doReceive){
            this.energy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(final int maxExtract, final boolean doExtract)
    {

        final int energyExtracted = MathHelper.minI(this.energy, MathHelper.minI(this.maxExtract, maxExtract));

        if (doExtract){
            this.energy -= energyExtracted;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored()
    {

        return this.energy;
    }

    @Override
    public int getMaxEnergyStored()
    {

        return this.capacity;
    }

}