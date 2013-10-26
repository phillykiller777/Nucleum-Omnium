/**
 * CCM Modding, Nucleum Omnium
 */
package ccm.nucleum.omnium.utils.helper;

import static ccm.nucleum.omnium.utils.helper.MathHelper.random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;

public final class FunctionHelper
{    
    /** Drops the Inventory that is contained in the {@link TileEntity} */
    public static void dropInventory(final World world, final int x, final int y, final int z)
    {
        final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null)
        {
            if (!(tileEntity instanceof IInventory))
            {
                CCMLogger.DEFAULT_LOGGER.debug("TILE %s WAS NOT AN INSTANCE OF IInventory, AND CALLED dropInventory!!!", tileEntity);
                return;
            }
            final IInventory inventory = (IInventory) tileEntity;
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                final ItemStack itemStack = inventory.getStackInSlot(i);
                if ((itemStack != null) && (itemStack.stackSize > 0))
                {
                    final float dX = (random().nextFloat() * 0.8F) + 0.1F;
                    final float dY = (random().nextFloat() * 0.8F) + 0.1F;
                    final float dZ = (random().nextFloat() * 0.8F) + 0.1F;
                    final EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));
                    if (itemStack.hasTagCompound())
                    {
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                    }
                    final float factor = 0.05F;
                    entityItem.motionX = random().nextGaussian() * factor;
                    entityItem.motionY = (random().nextGaussian() * factor) + 0.2F;
                    entityItem.motionZ = random().nextGaussian() * factor;
                    world.spawnEntityInWorld(entityItem);
                    itemStack.stackSize = 0;
                }
            }
        }
    }

    /**
     * @return true if there is Fire or Lava below. Otherwise false
     */
    public static boolean isFireBelow(final World world, final int xCoord, final int yCoord, final int zCoord)
    {
        final Block block = Block.blocksList[world.getBlockId(xCoord, yCoord, zCoord)];
        if (block.blockMaterial == Material.fire)
        {
            return true;
        } else if (block.blockMaterial == Material.lava)
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * @return true if the sun is visible. Otherwise false
     */
    public static boolean isSunVisible(final World world, final int xCoord, final int yCoord, final int zCoord)
    {
        return world.isDaytime() && !world.provider.hasNoSky && world.canBlockSeeTheSky(xCoord, yCoord, zCoord)
                && ((world.getWorldChunkManager().getBiomeGenAt(xCoord, yCoord) instanceof BiomeGenDesert) || (!world.isRaining() && !world.isThundering()));
    }
}